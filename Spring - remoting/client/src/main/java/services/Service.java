package services;


import domain.BaseEntity;

import java.util.Set;

public abstract class Service<ID, entity extends BaseEntity<ID>> implements ICrudService<ID, entity> {


    /**
     * Constructor for Service
     */
    protected Service() {

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
    public Set<entity> getAll() {
        return null;
    }

}
