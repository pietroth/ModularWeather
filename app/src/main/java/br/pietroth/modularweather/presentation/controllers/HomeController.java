package br.pietroth.modularweather.presentation.controllers;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.application.usecases.FetchTemperatureUseCase;
import br.pietroth.modularweather.application.usecases.FetchWeatherDescriptionUseCase;
import br.pietroth.modularweather.application.usecases.FetchWindContentUseCase;
import br.pietroth.modularweather.domain.valueobjects.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.WindInformations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final FetchTemperatureUseCase fetchTemperatureUseCase;
    private final FetchWindContentUseCase fetchWindContentUseCase;
    private final FetchWeatherDescriptionUseCase fetchWeatherDescriptionUseCase;

    public HomeController(
        FetchTemperatureUseCase fetchTemperatureUseCase,
        FetchWindContentUseCase fetchWindContentUseCase,
        FetchWeatherDescriptionUseCase fetchWeatherDescriptionUseCase
    ) {
        this.fetchTemperatureUseCase = fetchTemperatureUseCase;
        this.fetchWindContentUseCase = fetchWindContentUseCase;
        this.fetchWeatherDescriptionUseCase = fetchWeatherDescriptionUseCase;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "city", defaultValue = "Nova Iorque") String city, Model model) {
        model.addAttribute("city", city);

        try {
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

