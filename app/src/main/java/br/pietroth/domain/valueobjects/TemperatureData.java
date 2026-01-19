package br.pietroth.domain.valueobjects;

public class TemperatureData {
    private final double current;
    private final double max;
    private final double min;

    public TemperatureData(double current, double max, double min) {
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
