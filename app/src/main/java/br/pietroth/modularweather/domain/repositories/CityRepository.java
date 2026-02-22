package br.pietroth.modularweather.domain.repositories;

import java.util.List;

import br.pietroth.modularweather.domain.valueobjects.location.City;

public interface CityRepository {
    List<City> findByNamePrefix(String namePrefix);
    City findById(String cityId);
}
