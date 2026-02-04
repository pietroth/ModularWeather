package br.pietroth.modularweather.infrastructure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import br.pietroth.modularweather.domain.services.UserKeyStore;

public class PropertiesUserKeyStore 
    implements UserKeyStore<String, String> {

    private Properties properties;
    private final String configFilePath = "./properties/config.properties";

    public PropertiesUserKeyStore() {
        this.properties = new Properties();
        
        try (InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties não encontrado");
            }

            properties.load(is);

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
        properties.setProperty(key, value);

        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "User configuration file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void revoke(String key) {
        register(key, "");
    }
}

