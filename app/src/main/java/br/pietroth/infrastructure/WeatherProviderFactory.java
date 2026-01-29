package br.pietroth.infrastructure;

import br.pietroth.domain.services.UserKeyStore;
import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.infrastructure.adapters.OpenWeatherMapAdapter;

public class WeatherProviderFactory {
    public static enum Adapters {
        OPEN_WEATHER_MAP;
    }

    private OpenWeatherMapAdapter createOpenWeatherMapAdapter(UserKeyStore<String, String> userKeyStore) {
        return new OpenWeatherMapAdapter
            (userKeyStore, "user.key.open_weather_map");
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
