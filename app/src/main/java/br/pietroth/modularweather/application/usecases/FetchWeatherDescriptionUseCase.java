package br.pietroth.modularweather.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.services.WeatherProvider;

public class FetchWeatherDescriptionUseCase {
    private final WeatherProvider weatherProvider;

    public FetchWeatherDescriptionUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<String> execute(String city) {
        return weatherProvider.getWeatherDescriptionAsync(city);
    }
}
