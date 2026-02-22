package br.pietroth.modularweather.presentation.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.application.usecases.FetchTemperatureUseCase;
import br.pietroth.modularweather.application.usecases.FetchWeatherDescriptionUseCase;
import br.pietroth.modularweather.application.usecases.FetchWindContentUseCase;
import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.Parameter;
import br.pietroth.modularweather.domain.valueobjects.weather.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.weather.WindInformations;
import br.pietroth.modularweather.infrastructure.WeatherProviderFactory;
import br.pietroth.modularweather.infrastructure.models.configuration.SimpleParameter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    private static final String RAPIDAPI_KEY = "user.key.rapidapi_key";

    private final WeatherProviderFactory weatherProviderFactory;
    private final UserKeyStore<String, String> userKeyStore;

    public HomeController(
        WeatherProviderFactory weatherProviderFactory,
        UserKeyStore<String, String> userKeyStore
    ) {
        this.weatherProviderFactory = weatherProviderFactory;
        this.userKeyStore = userKeyStore;
    }

    @GetMapping("/")
    public String home(
        @RequestParam(name = "city", defaultValue = "Nova Iorque") String city,
        @RequestParam(name = "adapter", defaultValue = "OPEN_WEATHER_BY_QUANTUM_APIs") String adapter,
        @RequestParam(name = "keySaved", defaultValue = "false") boolean keySaved,
        @RequestParam(name = "keyError", defaultValue = "false") boolean keyError,
        @RequestParam Map<String, String> allParams,
        Model model
    ) {
        model.addAttribute("city", city);
        model.addAttribute("adapter", adapter);
        model.addAttribute("adapters", Arrays.asList(WeatherProviderFactory.Adapters.values()));
        model.addAttribute("keySaved", keySaved);
        model.addAttribute("keyError", keyError);

        WeatherProviderFactory.Adapters selectedAdapter = getSelectedAdapter(adapter);
        List<Parameter> defaultParams = weatherProviderFactory.getDefaultParameters(selectedAdapter);
        
        // Capturar parâmetros dinâmicos enviados no request (param_<nome>)
        List<Parameter> providedParams = buildParametersFromRequest(allParams, defaultParams);
        model.addAttribute("apiParameters", defaultParams);
        model.addAttribute("apiParameterValues", buildParameterValueMap(providedParams));

        Optional<String> rapidApiKey = userKeyStore.get(RAPIDAPI_KEY);
        model.addAttribute("rapidApiKey", rapidApiKey.orElse(""));
        model.addAttribute("apiKeyMissing", rapidApiKey.isEmpty());

        if (rapidApiKey.isEmpty()) {
            model.addAttribute("hasError", false);
            return "home";
        }

        try {
            WeatherProvider weatherProvider = weatherProviderFactory.getProvider(selectedAdapter, providedParams);
            FetchWeatherDescriptionUseCase fetchWeatherDescriptionUseCase = new FetchWeatherDescriptionUseCase(weatherProvider);
            FetchTemperatureUseCase fetchTemperatureUseCase = new FetchTemperatureUseCase(weatherProvider);
            FetchWindContentUseCase fetchWindContentUseCase = new FetchWindContentUseCase(weatherProvider);

            CompletableFuture<String> weatherDescriptionFuture = fetchWeatherDescriptionUseCase.execute(city);
            CompletableFuture<TemperatureInformations> temperatureDataFuture = fetchTemperatureUseCase.execute(city);
            CompletableFuture<WindInformations> windDataFuture = fetchWindContentUseCase.execute(city);

            CompletableFuture.allOf(weatherDescriptionFuture, temperatureDataFuture, windDataFuture).join();

            String weatherDescription = weatherDescriptionFuture.join();
            TemperatureInformations temperatureData = temperatureDataFuture.join();
            WindInformations windData = windDataFuture.join();

            model.addAttribute("weatherDescription", weatherDescription);
            model.addAttribute("tempCurrent", temperatureData.getCurrent());
            model.addAttribute("tempMax", temperatureData.getMaximum());
            model.addAttribute("tempMin", temperatureData.getMinimum());
            model.addAttribute("windSpeed", windData.getSpeed());
            model.addAttribute("windDirection", windData.getDirection());
            model.addAttribute("hasError", false);
        } catch (Exception ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "home";
    }

    @PostMapping("/config/keys")
    public String registerAdapterKey(
        @RequestParam(name = "apiKey", defaultValue = "") String apiKey,
        @RequestParam(name = "city", defaultValue = "Nova Iorque") String city,
        @RequestParam Map<String, String> allParams,
        RedirectAttributes redirectAttributes
    ) {
        if (apiKey.isBlank()) {
            redirectAttributes.addAttribute("keyError", true);
            redirectAttributes.addAttribute("keySaved", false);
        } else {
            userKeyStore.register(RAPIDAPI_KEY, apiKey.trim());
            redirectAttributes.addAttribute("keySaved", true);
            redirectAttributes.addAttribute("keyError", false);
        }

        redirectAttributes.addAttribute("city", city);
        redirectAttributes.addAttribute("adapter", WeatherProviderFactory.Adapters.OPEN_WEATHER_BY_QUANTUM_APIs.name());
        
        // Passar parâmetros dinâmicos para o redirect
        allParams.forEach((key, value) -> {
            if (key.startsWith("param_")) {
                redirectAttributes.addAttribute(key, value);
            }
        });
        
        return "redirect:/";
    }

    @GetMapping("/api/adapter-parameters")
    public org.springframework.http.ResponseEntity<?> getAdapterParameters(
        @RequestParam(name = "adapter", defaultValue = "OPEN_WEATHER_BY_QUANTUM_APIs") String adapter
    ) {
        try {
            WeatherProviderFactory.Adapters selectedAdapter = getSelectedAdapter(adapter);
            List<Parameter> parameters = weatherProviderFactory.getDefaultParameters(selectedAdapter);
            
            List<Map<String, Object>> paramList = new ArrayList<>();
            for (Parameter param : parameters) {
                Map<String, Object> paramMap = new java.util.HashMap<>();
                paramMap.put("name", param.getName());
                paramMap.put("value", param.getValue());
                paramMap.put("required", param.isRequired());
                paramList.add(paramMap);
            }
            
            return org.springframework.http.ResponseEntity.ok(paramList);
        } catch (Exception ex) {
            return org.springframework.http.ResponseEntity.badRequest().build();
        }
    }

    private WeatherProviderFactory.Adapters getSelectedAdapter(String adapter) {
        try {
            return WeatherProviderFactory.Adapters.valueOf(adapter.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return WeatherProviderFactory.Adapters.OPEN_WEATHER_BY_QUANTUM_APIs;
        }
    }

    private List<Parameter> buildParametersFromRequest(Map<String, String> allParams, List<Parameter> defaultParams) {
        List<Parameter> result = new ArrayList<>();
        
        for (Parameter defaultParam : defaultParams) {
            String paramKey = "param_" + defaultParam.getName();
            String paramValue = allParams.getOrDefault(paramKey, null);
            
            if (paramValue != null && !paramValue.isBlank()) {
                result.add(new SimpleParameter<String>(defaultParam.getName(), paramValue.trim(), defaultParam.isRequired()));
            } else {
                // Usar o valor padrão se não foi fornecido
                result.add(new SimpleParameter<String>(defaultParam.getName(), (String) defaultParam.getValue(), defaultParam.isRequired()));
            }
        }
        
        return result;
    }

    private Map<String, String> buildParameterValueMap(List<Parameter> parameters) {
        Map<String, String> map = new java.util.HashMap<>();
        for (Parameter param : parameters) {
            map.put(param.getName(), String.valueOf(param.getValue()));
        }
        return map;
    }
}
