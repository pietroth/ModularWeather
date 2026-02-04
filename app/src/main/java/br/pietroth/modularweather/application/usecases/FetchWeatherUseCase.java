package br.pietroth.modularweather.application.usecases;

import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.TemperatureData;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.WindData;

public class FetchWeatherUseCase {
    private WeatherProvider weatherProvider;

    public FetchWeatherUseCase(WeatherProvider weatherProvider) {
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

