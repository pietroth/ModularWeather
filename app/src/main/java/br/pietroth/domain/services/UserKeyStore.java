package br.pietroth.domain.services;

import java.util.Optional;

public interface UserKeyStore<Tkey, Tvalue> {
    Optional<Tvalue> get(Tkey key);
    void register(Tkey key, Tvalue value);
    void revoke(Tkey key);
}
