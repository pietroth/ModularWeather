package br.pietroth.modularweather.domain.valueobjects;

public class WindData {
    private final double speed;
    private final double direction;

    public WindData(double speed, double direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }
}

