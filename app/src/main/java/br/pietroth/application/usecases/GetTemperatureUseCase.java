package br.pietroth.application.usecases;

import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;

public class GetTemperatureUseCase {
    private WeatherProvider weatherProvider;

    public GetTemperatureUseCase(WeatherProvider weatherProvider) {
        this.weatherProvider = weatherProvider;
    }

    public TemperatureData getTemperature()
    {
        try {
            TemperatureData response = weatherProvider.getTemperature();
            return response;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
