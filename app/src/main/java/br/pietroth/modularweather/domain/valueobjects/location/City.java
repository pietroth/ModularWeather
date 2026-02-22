package br.pietroth.modularweather.domain.valueobjects.location;

public class City {
    private final String name;
    private final Coordinates coordinates;

    public City(String name, Coordinates coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}

