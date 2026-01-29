package br.pietroth.domain.repositories;

import java.util.List;
import br.pietroth.domain.valueobjects.City;

public interface CityRepository {
    List<City> findByNamePrefix(String namePrefix);
    City findById(String cityId);
}