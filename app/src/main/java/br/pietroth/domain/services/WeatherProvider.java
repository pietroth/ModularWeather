package br.pietroth.domain.services;

import java.util.concurrent.CompletableFuture;

import br.pietroth.domain.valueobjects.TemperatureData;
import br.pietroth.domain.valueobjects.WeatherContent;
import br.pietroth.domain.valueobjects.WindData;

public interface WeatherProvider {
    CompletableFuture<TemperatureData> getTemperatureAsync(String city);
    CompletableFuture<WindData> getWindDataAsync(String city);
    CompletableFuture<String> getWeatherDescriptionAsync(String city);
    CompletableFuture<WeatherContent> getWeatherContentAsync(String city);
}
