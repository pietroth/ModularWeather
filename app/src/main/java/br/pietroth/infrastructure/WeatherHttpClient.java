package br.pietroth.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import br.pietroth.infrastructure.models.Url;

public class WeatherHttpClient {
    private final Url url;
    private HttpClient client;

    public WeatherHttpClient(Url url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
    }

    public String get() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(url.getUrl()))
        .headers("Accept", "application/json")
        .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        return response.body();
    }
}
