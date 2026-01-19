package br.pietroth.infrastructure;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import br.pietroth.application.ConfigurationStore;

public class PropertiesConfigurantionStore 
    implements ConfigurationStore<String, String> {

    private Properties properties;
    private final String configFilePath = "./properties/config.properties";

    public PropertiesConfigurantionStore() {
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
    public void save(String key, String value) {
        properties.setProperty(key, value);

        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "User configuration file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String key) {
        save(key, "");
    }

    @Override
    public void update(String key, String value) {
        save(key, value);
    }
    
}
