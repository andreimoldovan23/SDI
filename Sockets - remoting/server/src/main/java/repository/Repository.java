package repository;

import domain.BaseEntity;
import domain.Validators.ValidatorException;

import java.util.Optional;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 *
 *
 */
public interface Repository<ID, T extends BaseEntity<ID>> {

    /**
     * Find the entity with the given {@code id}.
     *
     * @param var1: id of type ID
     * @return an {@code Optional} encapsulating the entity with the given id.
     */
    Optional<T> findOne(ID var1);

    /**
     *
     * @return all entities.
     */
    Iterable<T> findAll();

    /**
     * Saves the given entity.
     *
     * @param var1: entity of type T
     * @return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     * @throws ValidatorException
     *             if the entity is not valid.
     */
    Optional<T> save(T var1) throws ValidatorException;

    /**
     * Removes the entity with the given id.
     *
     * @param var1: entity of type T
     * @return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     */
    Optional<T> delete(ID var1);

    /**
     * Updates the given entity.
     *
     * @param var1: entity of type T
     * @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
     *         entity.
     * @throws ValidatorException
     *             if the entity is not valid.
     */
    Optional<T> update(T var1) throws ValidatorException;
}

