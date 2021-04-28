package services;

import domain.BaseEntity;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ICrudService<ID, T extends BaseEntity<ID>> {
    int PORT = 15000;
    String HOST = "localhost";

    void Add(T entity) throws Exception;
    void Update(T entity) throws Exception;
    void Delete(ID id) throws Exception;
    CompletableFuture<Set<T>> getAll();
}
