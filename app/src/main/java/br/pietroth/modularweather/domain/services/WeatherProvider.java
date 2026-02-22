package br.pietroth.modularweather.domain.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.valueobjects.weather.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.weather.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.weather.WindInformations;

public interface WeatherProvider {
    CompletableFuture<TemperatureInformations> getTemperatureAsync(String city);
    CompletableFuture<WindInformations> getWindInformationsAsync(String city);
    CompletableFuture<String> getWeatherDescriptionAsync(String city);
    CompletableFuture<WeatherContent> getWeatherContentAsync(String city);
}


