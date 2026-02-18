package br.pietroth.modularweather.domain.services;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.valueobjects.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.WindInformations;

public interface WeatherProvider {
    CompletableFuture<TemperatureInformations> getTemperatureAsync(String city);
    CompletableFuture<WindInformations> getWindInformationsAsync(String city);
    CompletableFuture<String> getWeatherDescriptionAsync(String city);
    CompletableFuture<WeatherContent> getWeatherContentAsync(String city);
}


