package br.pietroth.modularweather.infrastructure.adapters;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.Map;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.domain.valueobjects.TemperatureData;
import br.pietroth.modularweather.domain.valueobjects.WeatherContent;
import br.pietroth.modularweather.domain.valueobjects.WindData;
import br.pietroth.modularweather.infrastructure.SimpleHttpClient;
import br.pietroth.modularweather.infrastructure.SimpleJsonParser;
import br.pietroth.modularweather.infrastructure.models.OpenWeatherMapResponse;
import br.pietroth.modularweather.infrastructure.models.Url;


public class OpenWeatherMapAdapter implements WeatherProvider {
    private String userKey;

    public OpenWeatherMapAdapter(UserKeyStore<String, String> userKeyManager, String ApiProp) {
        this.userKey = userKeyManager
                .get(ApiProp)
                .orElseThrow(() ->
                    new IllegalStateException(
                        "API key não configurada para: " + ApiProp
                    )
                );
    }

    public CompletableFuture<String> fetchCityWeather(String city) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

        Url url = new Url(
            "https://open-weather13.p.rapidapi.com/" +
            "city?city=" + encodedCity +
            "&lang=pt_br"
        );

        Map<String, String> headers = Map.of(
            "Accept", "application/json",
            "x-rapidapi-key", this.userKey,
            "x-rapidapi-host", "open-weather13.p.rapidapi.com"
        );

        return new SimpleHttpClient(url)
            .getAsync(headers);
    }

    @Override
    public CompletableFuture<WeatherContent> getWeatherContentAsync(String city) {
        
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    // Json parsing
                    SimpleJsonParser<OpenWeatherMapResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherMapResponse.class);

                    OpenWeatherMapResponse response = parser.parse(json);

                    TemperatureData tempData = new TemperatureData(
                        // Fahrenheit to Celsius
                        (response.main.temp - 32) * 5 / 9,
                        (response.main.temp_max - 32) * 5 / 9,
                        (response.main.temp_min - 32) * 5 / 9
                    );

                    WindData windData = new WindData(
                        response.wind.speed,
                        response.wind.deg
                    );

                    return new WeatherContent(
                        response.weather.get(0).description,
                        tempData,
                        windData
                    );
                
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<TemperatureData> getTemperatureAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    // Json parsing
                    SimpleJsonParser<OpenWeatherMapResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherMapResponse.class);

                    OpenWeatherMapResponse response = parser.parse(json);

                    return new TemperatureData(
                        // Fahrenheit to Celsius
                        (response.main.temp - 32) * 5 / 9,
                        (response.main.temp_max - 32) * 5 / 9,
                        (response.main.temp_min - 32) * 5 / 9
                    );
                
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<WindData> getWindDataAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    // Json parsing
                    SimpleJsonParser<OpenWeatherMapResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherMapResponse.class);

                    OpenWeatherMapResponse response = parser.parse(json);

                    return new WindData(
                        response.wind.speed,
                        response.wind.deg
                    );
                
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public CompletableFuture<String> getWeatherDescriptionAsync(String city) {
        return fetchCityWeather(city)
            .thenApply(json -> {
                try {
                    // Json parsing
                    SimpleJsonParser<OpenWeatherMapResponse> parser =
                        new SimpleJsonParser<>(OpenWeatherMapResponse.class);

                    OpenWeatherMapResponse response = parser.parse(json);

                    return response.weather.get(0).description;
                
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
    }
}
