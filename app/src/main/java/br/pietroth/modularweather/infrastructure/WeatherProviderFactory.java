package br.pietroth.modularweather.infrastructure;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.infrastructure.adapters.OpenWeatherMapAdapter;

public class WeatherProviderFactory {
    private final String defaultApiProp = "user.key.rapidapi_key";
    public static enum Adapters {
        OPEN_WEATHER_MAP;
    }

    private OpenWeatherMapAdapter createOpenWeatherMapAdapter(UserKeyStore<String, String> userKeyStore) {
        return new OpenWeatherMapAdapter
            (userKeyStore, defaultApiProp);
    }

    public WeatherProvider getProvider(Adapters adapter) {
        UserKeyStore<String, String> defaultUserKeyStore = new PropertiesUserKeyStore();

        switch (adapter) {
            case OPEN_WEATHER_MAP:
                return createOpenWeatherMapAdapter(defaultUserKeyStore);
        
            default:
                System.out.println("Valor inválido! Utilizando OpenWeatherMap como padrão.");
                return createOpenWeatherMapAdapter(defaultUserKeyStore);
        }
    }
}

