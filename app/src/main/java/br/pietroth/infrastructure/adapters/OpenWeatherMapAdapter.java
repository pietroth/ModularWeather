package br.pietroth.infrastructure.adapters;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.pietroth.application.ConfigurationStore;
import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;
import br.pietroth.infrastructure.WeatherHttpClient;
import br.pietroth.infrastructure.models.OpenWeatherMapResponse;
import br.pietroth.infrastructure.models.Url;

public class OpenWeatherMapAdapter implements WeatherProvider {
    private final WeatherHttpClient httpClient;
    private ConfigurationStore<String, String> configurationStore;

    String city = "São Paulo";
    String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);

    public OpenWeatherMapAdapter(ConfigurationStore<String, String> configurationStore, String ApiProp) {
        this.configurationStore = configurationStore;
        String userKey = configurationStore
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

        httpClient = new WeatherHttpClient(url);
    }

    @Override
    public TemperatureData getTemperature() {
        try {
            String json = httpClient.get();

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
            e.printStackTrace();
            return null;
        }
    }
}