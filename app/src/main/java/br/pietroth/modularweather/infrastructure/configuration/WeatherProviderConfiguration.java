package br.pietroth.modularweather.infrastructure.configuration;

import br.pietroth.modularweather.domain.services.UserKeyStore;
import br.pietroth.modularweather.infrastructure.PropertiesUserKeyStore;
import br.pietroth.modularweather.infrastructure.WeatherProviderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherProviderConfiguration {
    @Bean
    public UserKeyStore<String, String> userKeyStore() {
        return new PropertiesUserKeyStore();
    }

    @Bean
    public WeatherProviderFactory weatherProviderFactory() {
        return new WeatherProviderFactory();
    }
}
