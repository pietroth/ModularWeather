package br.pietroth.modularweather.infrastructure.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {
    public Main main;
    public Wind wind;
    public List<Weather> weather;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        public double temp;
        public double temp_max;
        public double temp_min;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        public double speed;
        public double deg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        public String description;
    }
}

