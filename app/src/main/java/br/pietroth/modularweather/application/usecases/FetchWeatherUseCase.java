package br.pietroth.modularweather.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;

public class FetchWeatherUseCase {
    private final WeatherProvider weatherProvider;

    public FetchWeatherUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<WeatherContent> execute(String city)
    {
        return weatherProvider.getWeatherContentAsync(city);
    }
}