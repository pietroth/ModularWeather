package br.pietroth.modularweather.infrastructure.configuration;

import br.pietroth.modularweather.application.usecases.FetchTemperatureUseCase;
import br.pietroth.modularweather.application.usecases.FetchWeatherUseCase;
import br.pietroth.modularweather.application.usecases.FetchWeatherDescriptionUseCase;
import br.pietroth.modularweather.application.usecases.FetchWindContentUseCase;
import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.domain.services.WeatherProvider;
import br.pietroth.modularweather.infrastructure.PropertiesUserKeyStore;
import br.pietroth.modularweather.infrastructure.adapters.OpenWeatherMapAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherProviderConfiguration {
    private static final String DEFAULT_API_PROP = "user.key.rapidapi_key";

    @Bean
    public UserKeyStore<String, String> userKeyStore() {
        return new PropertiesUserKeyStore();
    }

    @Bean
    public WeatherProvider weatherProvider(UserKeyStore<String, String> userKeyStore) {
        return new OpenWeatherMapAdapter(userKeyStore, DEFAULT_API_PROP);
    }

    @Bean
    public FetchWeatherUseCase fetchWeatherUseCase(WeatherProvider weatherProvider) {
        return new FetchWeatherUseCase(weatherProvider);
    }

    @Bean
    public FetchTemperatureUseCase fetchTemperatureUseCase(WeatherProvider weatherProvider) {
        return new FetchTemperatureUseCase(weatherProvider);
    }

    @Bean
    public FetchWindContentUseCase fetchWindContentUseCase(WeatherProvider weatherProvider) {
        return new FetchWindContentUseCase(weatherProvider);
    }

    @Bean
    public FetchWeatherDescriptionUseCase fetchWeatherDescriptionUseCase(WeatherProvider weatherProvider) {
        return new FetchWeatherDescriptionUseCase(weatherProvider);
    }
}
