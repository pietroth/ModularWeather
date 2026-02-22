package br.pietroth.modularweather.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.Parameter;
import br.pietroth.modularweather.infrastructure.adapters.OpenWeatherByQuantumAPIs;
import br.pietroth.modularweather.infrastructure.adapters.WeatherAPIByMarufBelete;
import br.pietroth.modularweather.infrastructure.models.configuration.SimpleParameter;

public class WeatherProviderFactory {
    private static final String RAPIDAPI_KEY_PROP = "user.key.rapidapi_key";

    public static enum Adapters {
        OPEN_WEATHER_BY_QUANTUM_APIs("OpenWeather by Quantum APIs"),
        WEATHER_API_BY_MARUF_BELETE("WeatherAPI by Maruf Belete");

        private final String displayName;

        Adapters(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private OpenWeatherByQuantumAPIs createOpenWeatherByQuantumAPIs(
        UserKeyStore<String, String> userKeyStore,
        List<Parameter> parameters
    ) {
        String language = findStringParameter(parameters, "language")
            .or(() -> findStringParameter(parameters, "lang"))
            .orElse("en");

        return new OpenWeatherByQuantumAPIs(userKeyStore, RAPIDAPI_KEY_PROP, language);
    }

    private WeatherAPIByMarufBelete createWeatherAPIByMarufBelete(
        UserKeyStore<String, String> userKeyStore,
        List<Parameter> parameters
    ) {
        String units = findStringParameter(parameters, "units").orElse("metric");
        String language = findStringParameter(parameters, "language")
            .or(() -> findStringParameter(parameters, "lang"))
            .orElse("en");

        return new WeatherAPIByMarufBelete(userKeyStore, RAPIDAPI_KEY_PROP, units, language);
    }

    private Optional<String> findStringParameter(List<Parameter> parameters, String parameterName) {
        return parameters.stream()
            .filter(Objects::nonNull)
            .filter(parameter -> parameterName.equalsIgnoreCase(parameter.getName()))
            .map(Parameter::getValue)
            .filter(Objects::nonNull)
            .map(String::valueOf)
            .map(String::trim)
            .filter(value -> !value.isEmpty())
            .findFirst();
    }

    public List<Parameter> getDefaultParameters(Adapters adapter) {
        switch (adapter) {
            case OPEN_WEATHER_BY_QUANTUM_APIs:
                return Arrays.asList(
                    new SimpleParameter<String>("language", "en", false)
                );
            case WEATHER_API_BY_MARUF_BELETE:
                return Arrays.asList(
                    new SimpleParameter<String>("units", "metric", false),
                    new SimpleParameter<String>("language", "en", false)
                );
            default:
                return Arrays.asList(
                    new SimpleParameter<String>("language", "en", false)
                );
        }
    }

    public WeatherProvider getProvider(Adapters adapter, List<Parameter> parameters) {
        Objects.requireNonNull(parameters, "A lista de parametros nao pode ser nula.");

        UserKeyStore<String, String> defaultUserKeyStore = new PropertiesUserKeyStore();

        switch (adapter) {
            case OPEN_WEATHER_BY_QUANTUM_APIs:
                return createOpenWeatherByQuantumAPIs(defaultUserKeyStore, parameters);
            case WEATHER_API_BY_MARUF_BELETE:
                return createWeatherAPIByMarufBelete(defaultUserKeyStore, parameters);
            default:
                System.out.println("Valor invalido! Utilizando OpenWeatherByQuantum como padrao.");
                return createOpenWeatherByQuantumAPIs(defaultUserKeyStore, parameters);
        }
    }
}
