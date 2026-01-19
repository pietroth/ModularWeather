package br.pietroth.application;

import java.util.Optional;

public interface ConfigurationStore<Tkey, Tvalue> {
    Optional<Tvalue> get(Tkey key);
    void save(Tkey key, Tvalue value);
    void update(Tkey key, Tvalue value);
    void remove(Tkey key);
}
