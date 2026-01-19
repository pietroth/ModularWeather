package br.pietroth;

import br.pietroth.application.usecases.GetTemperatureUseCase;
import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.domain.valueobjects.TemperatureData;
import br.pietroth.infrastructure.WeatherProviderFactory;

public class App {
    public static void main(String[] args) {
        String option = args.length > 0 ? args[0] : "1";

        GetTemperatureUseCase weatherService;
        WeatherProvider provider;
        WeatherProviderFactory providerFactory = new WeatherProviderFactory();
        
        provider = providerFactory.getProvider(Integer.parseInt(option));
        weatherService = new GetTemperatureUseCase(provider);
        TemperatureData response = weatherService.getTemperature();

        System.out.println(
            String.format("Temperatura atual: %.1f°C; Mínima: %.1f°C; Máxima: %.1f°C", 
            response.getCurrent(), response.getMinimum(), response.getMaximum()));
    }
}
