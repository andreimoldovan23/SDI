package repository;

import domain.Client;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;

import java.sql.*;

public class ClientDbRepository extends DBRepo<Integer, Client> {
    /**
     * Constructor for the DBRepoClient class. Either loads data from database
     * or creates a table for the client entities if it does not exist
     *
     * @param validator : a validator for the client entities from the repo
     * @param database  : the database name
     * @throws domain.Validators.ValidatorException
     *       if there are problems establishing a connection or accessing the database
     */
    public ClientDbRepository(Validator<Integer, Client> validator, String database) throws ValidatorException {
        super(validator, database);
    }

    /**
     * Method for creating a table in the database for clients
     * @throws ValidatorException
     *   if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE CLIENT" +
                "(ID INTEGER PRIMARY KEY," +
                "FIRSTNAME TEXT NOT NULL," +
                "LASTNAME TEXT NOT NULL," +
                "ADDRESS_ID INTEGER NOT NULL," +
                "AGE INTEGER," +
                "PHONE_NUMBER TEXT," +
                "FOREIGN KEY(ADDRESS_ID) REFERENCES ADDRESS(ID))";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throw new DatabaseException("Problem creating CLIENT table");
        }
    }

    /**
     * Method that returns the name of the client table
     * @return String: the name of the table
     */
    @Override
    protected String getTableName() {
        return "CLIENT";
    }

    /**
     * Method for loading data from the client table from the database into the local repository
     * @throws ValidatorException
     *    if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM CLIENT";
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int age = resultSet.getInt(5);
                Client client = Client.Builder()
                        .id(resultSet.getInt(1))
                        .firstName(resultSet.getString(2))
                        .lastName(resultSet.getString(3))
                        .addressId(resultSet.getInt(4))
                        .age(age == 0 ? null : age)
                        .phoneNumber(resultSet.getString(6))
                        .build();
                this.entities.putIfAbsent(client.getId(), client);
            }
        } catch (SQLException sqe) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());
            statement.setInt(4, entity.getAddressId());
            setOptionalStatement(statement, 5, entity.getAge(), true, Types.INTEGER);
            setOptionalStatement(statement, 6, entity.getPhoneNumber(), false, Types.BLOB);
            statement.executeUpdate();
        } catch (SQLException se) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setInt(3, entity.getAddressId());
            setOptionalStatement(statement, 4, entity.getAge(), true, Types.INTEGER);
            setOptionalStatement(statement, 5, entity.getPhoneNumber(), false, Types.BLOB);
            statement.setInt(6, entity.getId());
            statement.executeUpdate();
        } catch (SQLException se) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while deleting from CLIENT table");
        }
    }

}
