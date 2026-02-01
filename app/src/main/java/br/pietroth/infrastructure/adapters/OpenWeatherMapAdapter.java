package br.pietroth.infrastructure.adapters;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.pietroth.domain.services.UserKeyStore;
import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;
import br.pietroth.infrastructure.SimpleHttpClient;
import br.pietroth.infrastructure.models.OpenWeatherMapResponse;
import br.pietroth.infrastructure.models.Url;
import java.util.Map;

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

    @Override
    public CompletableFuture<TemperatureData> getTemperatureAsync(String city) {
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

        SimpleHttpClient httpClient = new SimpleHttpClient(url);
        return httpClient.getAsync(headers)
            .thenApply(json -> {
                try {
                    // Json parsing
                    ObjectMapper mapper = new ObjectMapper();
                    OpenWeatherMapResponse response = 
                        mapper.readValue(json, OpenWeatherMapResponse.class);

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
}