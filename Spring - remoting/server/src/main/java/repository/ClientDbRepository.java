package repository;

import domain.Client;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

public class ClientDbRepository extends DBRepo<Integer, Client> {

    /**
     * Constructor for the DBRepoClient class. Either loads data from database
     * or creates a table for the client entities if it does not exist
     *
     * @param validator : a validator for the client entities from the repo
     * @throws domain.Validators.ValidatorException
     *       if there are problems establishing a connection or accessing the database
     */
    public ClientDbRepository(Validator<Integer, Client> validator, JdbcOperations jdbcOperations) throws ValidatorException {
        super(validator, jdbcOperations);
    }

    /**
     * Method for creating a table in the database for clients
     * @throws ValidatorException
     *   if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE IF NOT EXISTS CLIENT" +
                "(ID INTEGER PRIMARY KEY," +
                "FIRSTNAME TEXT NOT NULL," +
                "LASTNAME TEXT NOT NULL," +
                "ADDRESS_ID INTEGER NOT NULL," +
                "AGE INTEGER," +
                "PHONE_NUMBER TEXT," +
                "FOREIGN KEY(ADDRESS_ID) REFERENCES ADDRESS(ID))";
        try {
            jdbcOperations.execute(query);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Problem creating CLIENT table");
        }
    }

    /**
     * Drops the CLIENT table from the database
     */
    @Override
    public void deleteTable() {
        String query = "DELETE FROM CLIENT";
        jdbcOperations.update(query);
        this.entities.clear();
    }

    /**
     * Method for loading data from the client table from the database into the local repository
     * @throws ValidatorException
     *    if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM CLIENT";
        try {
            List<Client> clients = jdbcOperations.query(query, (rs, rowNum) -> {
                int age = rs.getInt("AGE");
                return Client.Builder()
                        .id(rs.getInt("ID"))
                        .firstName(rs.getString("FIRSTNAME"))
                        .lastName(rs.getString("LASTNAME"))
                        .addressId(rs.getInt("ADDRESS_ID"))
                        .age(age == 0 ? null : age)
                        .phoneNumber(rs.getString("PHONE_NUMBER"))
                        .build();
            });
            clients.forEach(client -> this.entities.putIfAbsent(client.getId(), client));
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while reading data from CLIENT table");
        }
    }

    /**
     * Method for executing an insert query on the CLIENT table of the database
     * @param entity : The address to be inserted
     * @throws ValidatorException
     *      if there are problems inserting data into the database
     */
    @Override
    protected void executeInsertQuery(Client entity) throws ValidatorException {
        String query = "INSERT INTO CLIENT (ID, FIRSTNAME, LASTNAME, ADDRESS_ID, AGE, PHONE_NUMBER) VALUES (?,?,?,?,?,?)";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getId(), entity.getFirstName(), entity.getLastName(),
                    entity.getAddressId(), entity.getAge(), entity.getPhoneNumber());
        } catch (DataAccessException se) {
            throw new DatabaseException("Something went wrong while inserting into CLIENT table");
        }
    }

    /**
     * Method for executing an update query on the CLIENT table of the database
     * @param entity : The entity to be updated
     * @throws ValidatorException
     *      if there are problems updating data in the database
     */
    @Override
    protected void executeUpdateQuery(Client entity) throws ValidatorException {
        String query = "UPDATE CLIENT SET FIRSTNAME = ?, LASTNAME = ?, ADDRESS_ID = ?," +
                "AGE = ?, PHONE_NUMBER = ? WHERE ID = ?";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getFirstName(), entity.getLastName(),
                    entity.getAddressId(), entity.getAge(), entity.getPhoneNumber(), entity.getId());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while updating CLIENT table");
        }
    }

    /**
     * Method for executing a delete query on the CLIENT table of the database
     * @param integer : The id of the entity to be deleted
     * @throws ValidatorException
     *      if there are problems deleting data from the database
     */
    @Override
    protected void executeDeleteQuery(Integer integer) throws ValidatorException {
        String query = "DELETE FROM CLIENT WHERE ID = ?";
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, integer);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while deleting from CLIENT table");
        }
    }

}
