package repository;

import domain.BaseEntity;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Generic class which the Repository interface. Provides specialized behaviour for working with databases
 */
public abstract class DBRepo<ID, T extends BaseEntity<ID>> implements Repository<ID, T> {

    protected JdbcOperations jdbcOperations;
    protected Map<ID, T> entities;
    protected Validator<ID, T> validator;
    protected String activateConstraints;

    /**
     * Constructor for the DBRepo classes. Either loads data from database
     * or creates a table for the entities the repo works with if it does not exist
     * @param validator: a validator for the entities from the repo
     * @throws ValidatorException
     *          if there were problems establishing a connection or reading data from the database
     */
    protected DBRepo(Validator<ID, T> validator, JdbcOperations jdbcOperations) throws ValidatorException {
        activateConstraints = "PRAGMA foreign_keys= ON";
        this.jdbcOperations = jdbcOperations;
        this.validator = validator;
        this.entities = new ConcurrentHashMap<>();
        createTable();
        loadData();
    }

    /**
     * Finds an object by its id
     * @param var1 of type ID
     * @return an {@code Optional<T>}, the object we are searching for or null
     */
    @Override
    public Optional<T> findOne(ID var1) {
        return Optional.ofNullable(this.entities.get(var1));
    }

    /**
     * Finds all objects
     * @return {@code Iterable<T>}, all objects
     */
    @Override
    public Iterable<T> findAll() {
        return new HashSet<>(this.entities.values());
    }

    /**
     * Overrides the save method of its parent. If the entity does not exist also saves it in the database
     * @param entity of type T
     * @return {@code Optional<T>} an optional containing the entity if it already existed, null otherwise
     * @throws ValidatorException
     *          if there are validation errors or database problems
     */
    @Override
    public Optional<T> save(T entity) throws ValidatorException {
        this.validator.validate(entity);
        executeInsertQuery(entity);
        return Optional.ofNullable(this.entities.putIfAbsent(entity.getId(), entity));
    }

    /**
     * Overrides the delete method of its parent. If the entity does exist also removes it from the database
     * @param id of type ID
     * @return {@code Optional<T>} an optional containing the entity if it was removed, null otherwise
     * @throws ValidatorException
     *          if there are database problems
     */
    @Override
    public Optional<T> delete(ID id) throws ValidatorException {
        executeDeleteQuery(id);
        return Optional.ofNullable(this.entities.remove(id));
    }

    /**
     * Overrides the update method of its parent. If the entity was updated also updates it in the database
     * @param entity of type T
     * @return {@code Optional<T>} an optional containing the entity if it was updated, null otherwise
     * @throws ValidatorException
     *          if there were validation errors or database problems
     */
    @Override
    public Optional<T> update(T entity) throws ValidatorException {
        this.validator.validate(entity);
        executeUpdateQuery(entity);
        return Optional.ofNullable(this.entities.computeIfPresent(entity.getId(), (k, v) -> entity));
    }

    /**
     * Method for creating a table in the database for the entities the repo is working with
     * @throws ValidatorException
     *      if there are problems creating tables in the database
     */
    abstract protected void createTable() throws ValidatorException;

    /**
     * Method for loading data from the database into the local repository
     * @throws ValidatorException
     *      if there are problems reading data from the database
     */
    abstract protected void loadData() throws ValidatorException;

    /**
     * Method for executing an insert query on the database
     * @param entity : The entity to be inserted
     * @throws ValidatorException
     *      if there are problems inserting data into the database
     */
    abstract protected void executeInsertQuery(T entity) throws ValidatorException;

    /**
     * Method for executing an update query on the database
     * @param entity : The entity to be updated
     * @throws ValidatorException
     *      if there are problems updating data in the database
     */
    abstract protected void executeUpdateQuery(T entity) throws ValidatorException;

    /**
     * Method for executing an delete query on the database
     * @param id : The id of the entity to be deleted
     * @throws ValidatorException
     *      if there are problems deleting data from the database
     */
    abstract protected void executeDeleteQuery(ID id) throws ValidatorException;

    /**
     * Drops the table corresponding to the entity from the database
     */
    abstract public void deleteTable();

}
