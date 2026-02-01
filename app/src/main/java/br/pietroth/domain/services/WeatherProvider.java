package br.pietroth.domain.services;

import java.util.concurrent.CompletableFuture;

import br.pietroth.domain.valueobjects.TemperatureData;

public interface WeatherProvider {
    CompletableFuture<TemperatureData> getTemperatureAsync(String city);
}
