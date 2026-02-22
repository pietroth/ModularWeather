package br.pietroth.modularweather.infrastructure.adapters;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.Configurable;
import br.pietroth.modularweather.domain.valueobjects.weather.TemperatureInformations;
import br.pietroth.modularweather.domain.valueobjects.weather.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.weather.WindInformations;
import br.pietroth.modularweather.infrastructure.SimpleHttpClient;
import br.pietroth.modularweather.infrastructure.SimpleJsonParser;
import br.pietroth.modularweather.infrastructure.models.configuration.Url;
import br.pietroth.modularweather.infrastructure.models.dtos.WeatherAPIByMarufBeleteResponse;
import br.pietroth.modularweather.infrastructure.models.configuration.SimpleParameter;
import br.pietroth.modularweather.domain.valueobjects.Parameter;

public class WeatherAPIByMarufBelete implements WeatherProvider, Configurable {
    private final String userKey;
    private final String apiProp;
    private List<Parameter> parameters;

    public WeatherAPIByMarufBelete(
        UserKeyStore<String, String> userKeyStore, 
        String apiProp,
        String units,
        String language
    ) 
    {
        this.apiProp = apiProp;
        this.userKey = userKeyStore.get(apiProp).orElse(null);
        this.parameters = List.of(
            new SimpleParameter<String>("units", units, false),
            new SimpleParameter<String>("language", language, false)
        );
    }

    @Override
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
            .filter(p -> p.getName().equals("language"))
            .findFirst()
            .map(Parameter::getValue)
            .orElse("en");

        String units = (String) parameters.stream()
            .filter(p -> p.getName().equals("units"))
            .findFirst()
            .map(Parameter::getValue)
            .orElse("standard");

        Url url = new Url(
            "https://weather-api167.p.rapidapi.com/api/weather/forecast?place=" + encodedCity +
            "&cnt=3&units=" + units + "&type=three_hour&mode=json&lang=" + language
        );

        Map<String, String> headers = Map.of(
            "Accept", "application/json",
            "Content-Type", "application/json",
            "x-rapidapi-key", this.userKey,
            "x-rapidapi-host", "weather-api167.p.rapidapi.com"
        );

        return new SimpleHttpClient(url).getAsync(headers);
    }

    @Override
    public CompletableFuture<TemperatureInformations> getTemperatureAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<WeatherAPIByMarufBeleteResponse> parser =
                        new SimpleJsonParser<>(WeatherAPIByMarufBeleteResponse.class);

                    WeatherAPIByMarufBeleteResponse response = parser.parse(json);
                    WeatherAPIByMarufBeleteResponse.Entry forecastEntry = firstEntry(response);

                    return new TemperatureInformations(
                        forecastEntry.main.temperature,
                        forecastEntry.main.temperatureMax,
                        forecastEntry.main.temperatureMin
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
                    SimpleJsonParser<WeatherAPIByMarufBeleteResponse> parser =
                        new SimpleJsonParser<>(WeatherAPIByMarufBeleteResponse.class);

                    WeatherAPIByMarufBeleteResponse response = parser.parse(json);
                    WeatherAPIByMarufBeleteResponse.Entry forecastEntry = firstEntry(response);

                    return new WindInformations(
                        forecastEntry.wind.speed,
                        resolveDirection(forecastEntry.wind)
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
                    SimpleJsonParser<WeatherAPIByMarufBeleteResponse> parser =
                        new SimpleJsonParser<>(WeatherAPIByMarufBeleteResponse.class);

                    WeatherAPIByMarufBeleteResponse response = parser.parse(json);
                    WeatherAPIByMarufBeleteResponse.Entry forecastEntry = firstEntry(response);
                    if (forecastEntry.weather == null || forecastEntry.weather.isEmpty()) {
                        throw new IllegalStateException("WeatherAPIByMarufBelete: campo weather vazio.");
                    }
                    return forecastEntry.weather.get(0).description;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<WeatherContent> getWeatherContentAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    SimpleJsonParser<WeatherAPIByMarufBeleteResponse> parser =
                        new SimpleJsonParser<>(WeatherAPIByMarufBeleteResponse.class);

                    WeatherAPIByMarufBeleteResponse response = parser.parse(json);
                    WeatherAPIByMarufBeleteResponse.Entry forecastEntry = firstEntry(response);

                    TemperatureInformations tempData = new TemperatureInformations(
                        forecastEntry.main.temperature,
                        forecastEntry.main.temperatureMax,
                        forecastEntry.main.temperatureMin
                    );

                    WindInformations windData = new WindInformations(
                        forecastEntry.wind.speed,
                        resolveDirection(forecastEntry.wind)
                    );

                    if (forecastEntry.weather == null || forecastEntry.weather.isEmpty()) {
                        throw new IllegalStateException("WeatherAPIByMarufBelete: campo weather vazio.");
                    }
                    return new WeatherContent(
                        forecastEntry.weather.get(0).description,
                        tempData,
                        windData
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }

    private WeatherAPIByMarufBeleteResponse.Entry firstEntry(WeatherAPIByMarufBeleteResponse response) {
        if (response == null) {
            throw new IllegalStateException("WeatherAPIByMarufBelete: resposta vazia.");
        }

        WeatherAPIByMarufBeleteResponse.Entry forecastEntry = new WeatherAPIByMarufBeleteResponse.Entry();
        if (response.list != null && !response.list.isEmpty()) {
            forecastEntry = response.list.get(0);
        }

        if (forecastEntry.main == null) {
            forecastEntry.main = response.main;
        }
        if (forecastEntry.wind == null) {
            forecastEntry.wind = response.wind;
        }
        if (forecastEntry.weather == null || forecastEntry.weather.isEmpty()) {
            forecastEntry.weather = response.weather;
        }

        if (forecastEntry.main == null) {
            throw new IllegalStateException("WeatherAPIByMarufBelete: campo main ausente na resposta.");
        }
        if (forecastEntry.wind == null) {
            throw new IllegalStateException("WeatherAPIByMarufBelete: campo wind ausente na resposta.");
        }
        return forecastEntry;
    }

    private double resolveDirection(WeatherAPIByMarufBeleteResponse.Wind wind) {
        if (wind != null && wind.degrees != null) {
            return wind.degrees;
        }
        return parseDirection(wind == null ? null : wind.direction);
    }

    private double parseDirection(String direction) {
        if (direction == null) {
            return 0.0;
        }

        String normalized = direction.trim().toUpperCase();

        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ignored) {
            switch (normalized) {
                case "N": return 0.0;
                case "NE": return 45.0;
                case "E": return 90.0;
                case "SE": return 135.0;
                case "S": return 180.0;
                case "SW": return 225.0;
                case "SOUTHWEST": return 225.0;
                case "WEST-SOUTHWEST": return 247.5;
                case "WSW": return 247.5;
                case "W": return 270.0;
                case "NW": return 315.0;
                default: return 0.0;
            }
        }
    }
}
