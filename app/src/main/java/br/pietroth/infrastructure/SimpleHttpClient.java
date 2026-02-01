package br.pietroth.infrastructure;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.Map;
import br.pietroth.infrastructure.models.Url;

public class SimpleHttpClient {
    private final Url url;
    private HttpClient client;

    public SimpleHttpClient(Url url) {
        this.url = url;
        this.client = HttpClient.newHttpClient();
    }

    public CompletableFuture<String> getAsync(Map<String, String> headers){
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url.getUrl()));

        headers.forEach(builder::header);
        
        HttpRequest request = builder.build();

        return client
            .sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }
}
