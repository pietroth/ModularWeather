package br.pietroth.domain.services;

import br.pietroth.domain.valueobjects.TemperatureData;

public interface WeatherProvider {
    TemperatureData getTemperature();
}
