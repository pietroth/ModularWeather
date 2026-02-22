package br.pietroth.modularweather.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.weather.TemperatureInformations;

public class FetchTemperatureUseCase {
    private final WeatherProvider weatherProvider;

    public FetchTemperatureUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<TemperatureInformations> execute(String city) {
        return weatherProvider.getTemperatureAsync(city);
    }
}

