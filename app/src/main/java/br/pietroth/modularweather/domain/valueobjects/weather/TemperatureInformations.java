package br.pietroth.modularweather.domain.valueobjects.weather;

public class TemperatureInformations {
    private final double current;
    private final double max;
    private final double min;

    public TemperatureInformations(double current, double max, double min) {
        this.current = current;
        this.max = max;
        this.min = min;
    }

    public double getCurrent() {
        return current;
    }

    public double getMaximum() {
        return max;
    }

    public double getMinimum() {
        return min;
    }
}


