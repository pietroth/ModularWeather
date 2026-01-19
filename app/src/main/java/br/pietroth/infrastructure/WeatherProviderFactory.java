package br.pietroth.infrastructure;

import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.infrastructure.adapters.OpenWeatherMapAdapter;

public class WeatherProviderFactory {
    private enum Adapters {
        OPEN_WEATHER_MAP(1);

        private final int value;

        Adapters(int value) {
            this.value = value;
        }

        public static Adapters getFromInteger(int value) {
            for (Adapters a : Adapters.values()) {
                if (a.value == value) return a;
            } 
            throw new IllegalArgumentException("Valor inválido : " + value);
        }
    }

    private OpenWeatherMapAdapter createOpenWeatherMapAdapter() {
        PropertiesConfigurantionStore propertiesConfigurantionStore = 
            new PropertiesConfigurantionStore();

        return new OpenWeatherMapAdapter
            (propertiesConfigurantionStore, "user.key.open_weather_map");
    }

    public WeatherProvider getProvider(int option) {
        Adapters adapter = Adapters.getFromInteger(option);

        switch (adapter) {
            case OPEN_WEATHER_MAP:
                return createOpenWeatherMapAdapter();
        
            default:
                System.out.println("Valor inválido! Utilizando OpenWeatherMap como padrão.");
                return createOpenWeatherMapAdapter();
        }
    }
}
