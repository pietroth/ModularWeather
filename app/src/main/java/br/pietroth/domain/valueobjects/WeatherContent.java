package br.pietroth.domain.valueobjects;

public class WeatherContent {
    private final String description;
    private final TemperatureData temperatureData;
    private final WindData windData;

    public WeatherContent(
        String description, TemperatureData temperatureData, WindData windData) 
    {
        this.description = description;
        this.temperatureData = temperatureData;
        this.windData = windData;
    }

    public TemperatureData getTemperatureData() {
        return temperatureData;
    }

    public WindData getWindData() {
        return windData;
    }

    public String getDescription() {
        return description;
    }
}
