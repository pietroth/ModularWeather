package br.pietroth.modularweather.infrastructure.models.dtos;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherAPIByMarufBeleteResponse {

    @JsonAlias({"forecast", "forecasts", "data"})
    public List<Entry> list;
    @JsonAlias({"main", "current", "temperature"})
    public Main main;
    @JsonAlias({"wind", "wind_info"})
    public Wind wind;
    @JsonAlias({"weather", "conditions"})
    public List<Weather> weather;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Entry {
        @JsonAlias({"main", "current", "temperature"})
        public Main main;
        @JsonAlias({"wind", "wind_info"})
        public Wind wind;
        @JsonAlias({"weather", "conditions"})
        public List<Weather> weather;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {

        @JsonAlias({"temprature", "temperature", "temp"})
        public double temperature;

        @JsonAlias({"temprature_max", "temperature_max", "temp_max"})
        public double temperatureMax;

        @JsonAlias({"temprature_min", "temperature_min", "temp_min"})
        public double temperatureMin;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        public double speed;
        public String direction;

        @JsonProperty("degrees")
        public Double degrees;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        public String description;
    }
}
