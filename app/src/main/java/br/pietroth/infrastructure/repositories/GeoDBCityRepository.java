package br.pietroth.infrastructure.repositories;

import java.util.List;
import br.pietroth.domain.repositories.CityRepository;
import br.pietroth.domain.valueobjects.City;

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
