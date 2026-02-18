package br.pietroth.modularweather.domain.valueobjects;

public class WeatherContent {
    private final String description;
    private final TemperatureInformations temperatureData;
    private final WindInformations windData;

    public WeatherContent(
        String description, TemperatureInformations temperatureData, WindInformations windData) 
    {
        this.description = description;
        this.temperatureData = temperatureData;
        this.windData = windData;
    }

    public TemperatureInformations getTemperatureInformations() {
        return temperatureData;
    }

    public WindInformations getWindInformations() {
        return windData;
    }

    public String getDescription() {
        return description;
    }
}


