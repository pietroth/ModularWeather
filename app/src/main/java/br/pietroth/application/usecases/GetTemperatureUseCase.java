package br.pietroth.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;

public class GetTemperatureUseCase {
    private WeatherProvider weatherProvider;

    public GetTemperatureUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<TemperatureData> getTemperatureAsync(String city)
    {
        return weatherProvider.getTemperatureAsync(city);
    }
}
