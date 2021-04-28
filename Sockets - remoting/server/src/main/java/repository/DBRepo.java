package repository;

import domain.BaseEntity;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import repository.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Generic class which the Repository interface. Provides specialized behaviour for working with databases
 */
public abstract class DBRepo<ID, T extends BaseEntity<ID>> implements Repository<ID, T> {

    /**
     * The name of the database
     */
    protected final String database;

    protected Map<ID, T> entities;
    protected Validator<ID, T> validator;

    /**
     * Constructor for the DBRepo classes. Either loads data from database
     * or creates a table for the entities the repo works with if it does not exist
     * @param validator: a validator for the entities from the repo
     * @param database: the database name
     * @throws ValidatorException
     *          if there were problems establishing a connection or reading data from the database
     */
    protected DBRepo(Validator<ID, T> validator, String database) throws ValidatorException {
        this.validator = validator;
        this.entities = new ConcurrentHashMap<>();
        this.database = database;
        Optional<Boolean> optional = Optional.of(doesTableExist());
        optional.filter(val -> val)
                .ifPresentOrElse(el -> loadData(), this::createTable);
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
     * Helper method that creates a connection to the database
     * @return Connection: connection to the database
     * @throws ValidatorException
     *          if the connection could not be established
     */
    protected Connection getConnection() throws ValidatorException {
        Connection conn;
        String db = "jdbc:sqlite:" + database;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(db);
            conn.createStatement().executeUpdate("PRAGMA foreign_keys= ON");
        } catch (Exception e) {
            throw new DatabaseException("Something went wrong while connecting");
        }
        return conn;
    }

    /**
     * Helper method that checks if a table exists in the database
     * @return Boolean: a flag telling if the table exists or not
     * @throws ValidatorException
     *                if the connection could not be established
     */
    protected Boolean doesTableExist() throws ValidatorException {
        boolean foundTable = false;
        ResultSet rs;
        try(Connection connection = getConnection()) {
            DatabaseMetaData md = connection.getMetaData();
            rs = md.getTables(connection.getCatalog(), null, "%", new String[] {"TABLE"});
            while(rs.next() && !foundTable) {
                foundTable = rs.getString(3).equals(getTableName());
            }
            rs.close();
        } catch (SQLException e) {
            throw new DatabaseException("Something went wrong while connecting");
        }
        return foundTable;
    }

    /**
     * Sets a parameter of a preparedStatement if it exists, sets null otherwise
     * @param statement: the prepared statement whose parameter must be set
     * @param position: the position of the parameter
     * @param value: the value of the parameter
     * @param isInteger: a flag telling if we are setting an Integer or a String
     * @param code: the code of the sql data type the parameter represents
     * @throws ValidatorException
     *          if there was an error while writing to the database
     */
    protected void setOptionalStatement(PreparedStatement statement, Integer position, Object value, Boolean isInteger, Integer code)
            throws ValidatorException {
        Optional.ofNullable(value).ifPresentOrElse(val -> {
            try {
                if (isInteger) {
                    statement.setInt(position, (Integer) val);
                } else {
                    statement.setString(position, (String) val);
                }
            } catch (SQLException throwables) {
                throw new DatabaseException(throwables.getMessage());
            }
        }, () -> {
            try {
                statement.setNull(position, code);
            } catch (SQLException throwables) {
                throw new DatabaseException(throwables.getMessage());
            }
        });
    }

    /**
     * Method for creating a table in the database for the entities the repo is working with
     * @throws ValidatorException
     *      if there are problems creating tables in the database
     */
    abstract protected void createTable() throws ValidatorException;

    /**
     * Method that returns the name of the entity, implicitly the name of the table representing it
     * @return String: the name of the table
     */
    abstract protected String getTableName();

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

}
