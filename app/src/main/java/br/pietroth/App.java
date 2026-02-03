package br.pietroth;

import br.pietroth.application.usecases.GetTemperatureUseCase;
import br.pietroth.domain.services.WeatherProvider;
import br.pietroth.infrastructure.WeatherProviderFactory;

public class App {
    public static void main(String[] args) {
        String option = args.length > 0 ? args[0] : "1";

        GetTemperatureUseCase weatherService;
        WeatherProvider provider;
        WeatherProviderFactory providerFactory = new WeatherProviderFactory();
        WeatherProviderFactory.Adapters adapter;

        switch (option) {
            case "1":
                adapter = WeatherProviderFactory.Adapters.OPEN_WEATHER_MAP;
                break;

            default:
                adapter = WeatherProviderFactory.Adapters.OPEN_WEATHER_MAP;
                break;
        }

        provider = providerFactory.getProvider(adapter);
        weatherService = new GetTemperatureUseCase(provider);
        weatherService.getWeatherContentAsync("Nova Iorque")
            .thenAccept(response -> {
                System.out.println(
                    String.format(
                        "Temperatura atual: %.1f°C; Máxima: %.1f°C; Mínima: %.1f°C; Vento: %.1f km/h; Ângulo: %.0f°; Descrição: %s",
                        response.getTemperatureData().getCurrent(),
                        response.getTemperatureData().getMaximum(),
                        response.getTemperatureData().getMinimum(),
                        response.getWindData().getSpeed(),
                        response.getWindData().getDirection(),
                        response.getDescription()
                    )
                );
            })
            .exceptionally(ex -> {
                System.err.println("Erro ao obter dados meteorológicos: " + ex.getMessage());
                return null;
            })
            .join();
    }
}
