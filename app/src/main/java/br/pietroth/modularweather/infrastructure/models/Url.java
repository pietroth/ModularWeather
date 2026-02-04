package br.pietroth.modularweather.infrastructure.models;

public class Url {
    private String url;

    public Url(String url) {
        validate(url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private void validate(String url) {
        // Not Implemented
    }
}

