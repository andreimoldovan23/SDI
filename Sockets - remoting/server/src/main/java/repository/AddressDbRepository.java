package repository;

import domain.Address;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;

import java.sql.*;

public class AddressDbRepository extends DBRepo<Integer, Address> {

    /**
     * Constructor for the DBRepoAddress class. Either loads data from database
     * or creates a table for the address entities if it does not exist
     *
     * @param validator : a validator for the address entities from the repo
     * @param database  : the database name
     * @throws ValidatorException
     *      if there are problems establishing a connection or accessing the database
     */
    public AddressDbRepository(Validator<Integer, Address> validator, String database) throws ValidatorException {
        super(validator, database);
    }

    /**
     * Method for creating a table in the database for addresses
     * @throws ValidatorException
     *      if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE ADDRESS" +
                "(ID INTEGER PRIMARY KEY," +
                "CITY TEXT NOT NULL," +
                "STREET TEXT NOT NULL," +
                "NUMBER INTEGER NOT NULL)";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throw new DatabaseException("Problem creating ADDRESS table");
        }
    }

    /**
     * Method that returns the name of the address table
     * @return String: the name of the table
     */
    @Override
    protected String getTableName() {
        return "ADDRESS";
    }

    /**
     * Method for loading data from the address table from the database into the local repository
     * @throws ValidatorException
     *      if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM ADDRESS";
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Address address = Address.Builder()
                        .id(resultSet.getInt(1))
                        .city(resultSet.getString(2))
                        .street(resultSet.getString(3))
                        .number(resultSet.getInt(4))
                        .build();
                this.entities.putIfAbsent(address.getId(), address);
            }
        } catch (SQLException sqe) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getCity());
            statement.setString(3, entity.getStreet());
            statement.setInt(4, entity.getNumber());
            statement.executeUpdate();
        } catch (SQLException se) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getCity());
            statement.setString(2, entity.getStreet());
            statement.setInt(3, entity.getNumber());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException se) {
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
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while deleting from ADDRESS table");
        }
    }

}
