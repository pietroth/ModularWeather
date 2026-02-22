package br.pietroth.modularweather.domain.valueobjects;

public interface Parameter {
    String getName();
    Object getValue();
    boolean isRequired();
}
