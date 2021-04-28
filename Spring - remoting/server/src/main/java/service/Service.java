package service;


import domain.BaseEntity;
import domain.Validators.ValidatorException;
import repository.Repository;
import services.ICrudService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class Service<ID, entity extends BaseEntity<ID>> implements ICrudService<ID, entity> {
    protected final Repository<ID, entity> repository;


    /**
     * Constructor for Serivce
     * @param constructorRepo of type {@code Repository<ID, entity>}
     */
    protected Service(Repository<ID, entity> constructorRepo) {
        repository = constructorRepo;
    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    public void Add(entity element) throws ValidatorException {
        repository.save(element);
    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */
    public void Update(entity element) throws ValidatorException {
        repository.update(element);
    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    public void Delete(ID id) throws ValidatorException {
        repository.delete(id);
    }

    /**
     * Gets all entities in a set
     * @return {@code CompletableFuture<Set<entity>>}
     */
    public Set<entity> getAll() {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .collect(Collectors.toSet());
    }

}
