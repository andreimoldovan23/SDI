package services;


import domain.BaseEntity;
import tcp.TcpClient;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class Service<ID, entity extends BaseEntity<ID>> implements ICrudService<ID, entity> {
    protected final ExecutorService executorService;
    protected TcpClient tcpClient;

    /**
     * Constructor for Service
     * @param executorService: an executor service for multi threading
     */
    protected Service(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }


    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    public void Add(entity element) {

    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */
    public void Update(entity element) {

    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    public void Delete(ID id) {

    }

    /**
     * Gets all entities in a set
     * @return {@code Set<entity>}
     */
    public CompletableFuture<Set<entity>> getAll() {
        return null;
    }

}
