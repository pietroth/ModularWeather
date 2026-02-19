package br.pietroth.modularweather.presentation.controllers;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.application.usecases.FetchTemperatureUseCase;
import br.pietroth.modularweather.application.usecases.FetchWeatherDescriptionUseCase;
import br.pietroth.modularweather.application.usecases.FetchWindContentUseCase;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.WindInformations;
import br.pietroth.modularweather.infrastructure.WeatherProviderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final WeatherProviderFactory weatherProviderFactory;

    public HomeController(WeatherProviderFactory weatherProviderFactory) {
        this.weatherProviderFactory = weatherProviderFactory;
    }

    @GetMapping("/")
    public String home(
        @RequestParam(name = "city", defaultValue = "Nova Iorque") String city,
        @RequestParam(name = "adapter", defaultValue = "OPEN_WEATHER_MAP") String adapter,
        Model model
    ) {
        model.addAttribute("city", city);
        model.addAttribute("adapter", adapter);

        try {
            WeatherProviderFactory.Adapters selectedAdapter;
            try {
                selectedAdapter = WeatherProviderFactory.Adapters.valueOf(adapter.toUpperCase());
            } catch (IllegalArgumentException ex) {
                selectedAdapter = WeatherProviderFactory.Adapters.OPEN_WEATHER_MAP;
                model.addAttribute("adapter", selectedAdapter.name());
            }

            WeatherProvider weatherProvider = weatherProviderFactory.getProvider(selectedAdapter);
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
}
