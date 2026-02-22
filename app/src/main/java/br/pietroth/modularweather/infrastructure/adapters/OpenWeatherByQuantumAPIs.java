package br.pietroth.modularweather.infrastructure.adapters;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.Configurable;
import br.pietroth.modularweather.domain.valueobjects.Parameter;
import br.pietroth.modularweather.domain.valueobjects.weather.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.weather.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.weather.WindInformations;
import br.pietroth.modularweather.infrastructure.SimpleHttpClient;
import br.pietroth.modularweather.infrastructure.SimpleJsonParser;
import br.pietroth.modularweather.infrastructure.models.configuration.SimpleParameter;
import br.pietroth.modularweather.infrastructure.models.configuration.Url;
import br.pietroth.modularweather.infrastructure.models.dtos.OpenWeatherByQuantumAPIsResponse;

public class OpenWeatherByQuantumAPIs implements WeatherProvider, Configurable {
    private final String userKey;
    private final String apiProp;
    private final List<Parameter> parameters;

    public OpenWeatherByQuantumAPIs(
        UserKeyStore<String, String> userKeyManager, 
        String apiProp,
        String language
    ) 
    {
        this.apiProp = apiProp;
        this.userKey = userKeyManager.get(apiProp).orElse(null);
        this.parameters = List.of(
            new SimpleParameter<String>("lang", language, false)
        );
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public CompletableFuture<String> fetchCityWeather(String city) {
        if (userKey == null || userKey.isBlank()) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("API key não configurada para: " + apiProp)
            );
        }

        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        String language = (String) parameters.stream()
            .filter(p -> p.getName().equals("lang"))
            .findFirst()
            .map(Parameter::getValue)
            .orElse("en");

        Url url = new Url(
            "https://open-weather13.p.rapidapi.com/" +
            "city?city=" + encodedCity +
            "&lang=" + language
        );

        Map<String, String> headers = Map.of(
            "Accept", "application/json",
            "x-rapidapi-key", this.userKey,
            "x-rapidapi-host", "open-weather13.p.rapidapi.com"
        );

        return new SimpleHttpClient(url).getAsync(headers);
    }

    @Override
    public CompletableFuture<WeatherContent> getWeatherContentAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<OpenWeatherByQuantumAPIsResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherByQuantumAPIsResponse.class);

                    OpenWeatherByQuantumAPIsResponse response = parser.parse(json);

                    TemperatureInformations tempData = new TemperatureInformations(
                        (response.main.temp - 32) * 5 / 9,
                        (response.main.temp_max - 32) * 5 / 9,
                        (response.main.temp_min - 32) * 5 / 9
                    );

                    WindInformations windData = new WindInformations(
                        response.wind.speed,
                        response.wind.deg
                    );

                    return new WeatherContent(
                        response.weather.get(0).description,
                        tempData,
                        windData
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<TemperatureInformations> getTemperatureAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<OpenWeatherByQuantumAPIsResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherByQuantumAPIsResponse.class);

                    OpenWeatherByQuantumAPIsResponse response = parser.parse(json);

                    return new TemperatureInformations(
                        (response.main.temp - 32) * 5 / 9,
                        (response.main.temp_max - 32) * 5 / 9,
                        (response.main.temp_min - 32) * 5 / 9
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<WindInformations> getWindInformationsAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<OpenWeatherByQuantumAPIsResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherByQuantumAPIsResponse.class);

                    OpenWeatherByQuantumAPIsResponse response = parser.parse(json);

                    return new WindInformations(
                        response.wind.speed,
                        response.wind.deg
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<String> getWeatherDescriptionAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<OpenWeatherByQuantumAPIsResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherByQuantumAPIsResponse.class);

                    OpenWeatherByQuantumAPIsResponse response = parser.parse(json);
                    return response.weather.get(0).description;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
