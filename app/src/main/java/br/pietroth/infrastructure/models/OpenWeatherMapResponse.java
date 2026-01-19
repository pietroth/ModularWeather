package br.pietroth.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {
    public Main main;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Main {
        public double temp;
        public double temp_max;
        public double temp_min;
    }
}