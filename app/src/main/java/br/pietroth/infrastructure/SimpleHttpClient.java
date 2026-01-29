package br.pietroth.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import br.pietroth.infrastructure.models.Url;

public class WeatherHttpClient {
    private final Url url;
    private HttpClient client;

    public WeatherHttpClient(Url url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
    }

    public CompletableFuture<String> getAsync(){
        HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(url.getUrl()))
        .headers("Accept", "application/json")
        .build();

        return client
            .sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }
}
