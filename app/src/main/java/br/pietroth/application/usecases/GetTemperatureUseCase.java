package br.pietroth.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;
import br.pietroth.domain.valueobjects.WeatherContent;
import br.pietroth.domain.valueobjects.WindData;

public class GetTemperatureUseCase {
    private WeatherProvider weatherProvider;

    public GetTemperatureUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public CompletableFuture<WeatherContent> getWeatherContentAsync(String city)
    {
        return weatherProvider.getWeatherContentAsync(city);
    }

    public CompletableFuture<TemperatureData> getTemperatureAsync(String city)
    {
        return weatherProvider.getTemperatureAsync(city);
    }

    public CompletableFuture<String> getWeatherDescriptionAsync(String city)
    {
        return weatherProvider.getWeatherDescriptionAsync(city);
    }

    public CompletableFuture<WindData> getWindDataAsync(String city)
    {
        return weatherProvider.getWindDataAsync(city);
    }

    public WeatherProvider getWeatherProvider() {
        return weatherProvider;
    }
}
