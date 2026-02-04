package br.pietroth.modularweather.domain.services;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.valueobjects.TemperatureData;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.WindData;

public interface WeatherProvider {
    CompletableFuture<TemperatureData> getTemperatureAsync(String city);
    CompletableFuture<WindData> getWindDataAsync(String city);
    CompletableFuture<String> getWeatherDescriptionAsync(String city);
    CompletableFuture<WeatherContent> getWeatherContentAsync(String city);
}

