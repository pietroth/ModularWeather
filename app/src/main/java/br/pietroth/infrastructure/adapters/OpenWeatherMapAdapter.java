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

public class OpenWeatherMapAdapter implements WeatherProvider {
    private final SimpleHttpClient httpClient;

    String city = "São Paulo";
    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

    public OpenWeatherMapAdapter(UserKeyStore<String, String> userKeyManager, String ApiProp) {
        String userKey = userKeyManager
                .get(ApiProp)
                .orElseThrow(() ->
                    new IllegalStateException(
                        "API key não configurada para: " + ApiProp
                    )
                );

        Url url = new Url(
            "https://api.openweathermap.org/data/2.5/weather" +
            "?q=" + encodedCity +
            "&appid=" + userKey +
            "&units=metric" +
            "&lang=pt_br"
        );
        System.out.println(url.getUrl());

        httpClient = new SimpleHttpClient(url);
    }

    @Override
    public CompletableFuture<TemperatureData> getTemperatureAsync() {
        return httpClient.getAsync()
            .thenApply(json -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    OpenWeatherMapResponse response = 
                        mapper.readValue(json, OpenWeatherMapResponse.class);

                    return new TemperatureData(
                        response.main.temp,
                        response.main.temp_max,
                        response.main.temp_min
                    );
                
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
    }
}