package repository;

import domain.Address;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

public class AddressDbRepository extends DBRepo<Integer, Address> {

    /**
     * Constructor for the DBRepoAddress class. Either loads data from database
     * or creates a table for the address entities if it does not exist
     *
     * @param validator : a validator for the address entities from the repo
     * @throws ValidatorException
     *      if there are problems establishing a connection or accessing the database
     */
    public AddressDbRepository(Validator<Integer, Address> validator, JdbcOperations jdbcOperations) throws ValidatorException {
        super(validator, jdbcOperations);
    }

    /**
     * Drops the ADDRESS table from the database
     */
    @Override
    public void deleteTable() {
        String query = "DELETE FROM ADDRESS";
        jdbcOperations.update(query);
        this.entities.clear();
    }

    /**
     * Method for creating a table in the database for addresses
     * @throws ValidatorException
     *      if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE IF NOT EXISTS ADDRESS" +
                "(ID INTEGER PRIMARY KEY," +
                "CITY TEXT NOT NULL," +
                "STREET TEXT NOT NULL," +
                "NUMBER INTEGER NOT NULL)";
        try {
            jdbcOperations.execute(query);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Problem creating ADDRESS table");
        }
    }

    /**
     * Method for loading data from the address table from the database into the local repository
     * @throws ValidatorException
     *      if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM ADDRESS";
        try {
            List<Address> addresses = jdbcOperations.query(query, (rs, rowNum) -> Address.Builder()
                    .id(rs.getInt("ID"))
                    .city(rs.getString("CITY"))
                    .street(rs.getString("STREET"))
                    .number(rs.getInt("NUMBER"))
                    .build());
            addresses.forEach(adr -> this.entities.putIfAbsent(adr.getId(), adr));
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while reading data from the ADDRESS table");
        }
    }

    /**
     * Method for executing an insert query on the ADDRESS table of the database
     * @param entity : The address to be inserted
     * @throws ValidatorException
     *      if there are problems inserting data into the database
     */
    @Override
    protected void executeInsertQuery(Address entity) throws ValidatorException {
        String query = "INSERT INTO ADDRESS (ID, CITY, STREET, NUMBER) VALUES (?,?,?,?)";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getId(), entity.getCity(), entity.getStreet(), entity.getNumber());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while inserting into ADDRESS table");
        }
    }

    /**
     * Method for executing an update query on the ADDRESS table of the database
     * @param entity : The entity to be updated
     * @throws ValidatorException
     *      if there are problems updating data in the database
     */
    @Override
    protected void executeUpdateQuery(Address entity) throws ValidatorException {
        String query = "UPDATE ADDRESS SET CITY = ?, STREET = ?, NUMBER = ? WHERE ID = ?";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getCity(), entity.getStreet(), entity.getNumber(), entity.getId());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while updating ADDRESS table");
        }
    }

    /**
     * Method for executing a delete query on the ADDRESS table of the database
     * @param integer : The id of the entity to be deleted
     * @throws ValidatorException
     *      if there are problems deleting data from the database
     */
    @Override
    protected void executeDeleteQuery(Integer integer) throws ValidatorException {
        String query = "DELETE FROM ADDRESS WHERE ID = ?";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, integer);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while deleting from ADDRESS table");
        }
    }

}
