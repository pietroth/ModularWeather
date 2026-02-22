package br.pietroth.modularweather.infrastructure.repositories;

import java.util.List;
import br.pietroth.modularweather.domain.repositories.CityRepository;
import br.pietroth.modularweather.domain.valueobjects.location.City;

public class GeoDBCityRepository implements CityRepository {
    
    @Override
    public List<City> findByNamePrefix(String namePrefix) {
        throw new UnsupportedOperationException("Unimplemented method 'findByNamePrefix'");
    }

    @Override
    public City findById(String cityId) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
    
}

