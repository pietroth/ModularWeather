package br.pietroth.modularweather.infrastructure.models.configuration;

import br.pietroth.modularweather.domain.valueobjects.Parameter;

public class SimpleParameter<Type> implements Parameter {
    private final String name;
    private final Type value;
    private final boolean required;

    public SimpleParameter(String name, Type value, boolean isRequired) {
        this.name = name;
        this.value = value;
        this.required = isRequired;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override   
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isRequired() {
        return required;
    }
}
