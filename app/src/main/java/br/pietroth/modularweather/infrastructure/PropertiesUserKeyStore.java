package br.pietroth.modularweather.infrastructure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

import br.pietroth.modularweather.domain.services.UserKeyStore;

public class PropertiesUserKeyStore 
    implements UserKeyStore<String, String> {

    private Properties properties;
    private final String configFilePath;

    public PropertiesUserKeyStore() {
        this.properties = new Properties();
        
        String userHome = System.getProperty("user.home");
        this.configFilePath = userHome + "/ModularWeather/config.properties";
        
        try {
            if (Files.exists(Paths.get(configFilePath))) {
                properties.load(Files.newInputStream(Paths.get(configFilePath)));
            } else {
                Files.createDirectories(Paths.get(configFilePath).getParent());
                saveProperties();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config.properties", e);
        }
    }
    
    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(properties.getProperty(key))
            .filter(value -> !value.isEmpty());
    }

    @Override
    public void register(String key, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Valor da chave não pode ser nulo.");
        }
        properties.setProperty(key, value);
        saveProperties();
    }

    private void saveProperties() {
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "User configuration file");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar config.properties", e);
        }
    }

    @Override
    public void revoke(String key) {
        register(key, "");
    }
}

