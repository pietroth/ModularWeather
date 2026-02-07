package br.pietroth.modularweather.presentation.controllers;

import br.pietroth.modularweather.application.usecases.FetchWeatherUseCase;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final FetchWeatherUseCase weatherService;

    public HomeController(FetchWeatherUseCase weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "city", defaultValue = "Nova Iorque") String city, Model model) {
        model.addAttribute("city", city);

        try {
            WeatherContent content = weatherService.getWeatherContentAsync(city).join();
            model.addAttribute("description", content.getDescription());
            model.addAttribute("tempCurrent", content.getTemperatureData().getCurrent());
            model.addAttribute("tempMax", content.getTemperatureData().getMaximum());
            model.addAttribute("tempMin", content.getTemperatureData().getMinimum());
            model.addAttribute("windSpeed", content.getWindData().getSpeed());
            model.addAttribute("windDirection", content.getWindData().getDirection());
            model.addAttribute("hasError", false);
        } catch (Exception ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "home";
    }
}
