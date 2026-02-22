package br.pietroth.modularweather.application.usecases;

import java.util.concurrent.CompletableFuture;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.weather.WindInformations;

public class FetchWindContentUseCase {
    private final WeatherProvider weatherProvider;

    public FetchWindContentUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<WindInformations> execute(String city)
    {
        return weatherProvider.getWindInformationsAsync(city);
    }
}

