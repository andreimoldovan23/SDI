package repository;

import domain.Coffee;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;

import java.sql.*;

public class CoffeeDbRepository extends DBRepo<Integer, Coffee> {
    /**
     * Constructor for the DBRepoCoffee class. Either loads data from database
     * or creates a table for the coffee entities if it does not exist
     *
     * @param validator : a validator for the coffee entities
     * @param database  : the database name
     * @throws ValidatorException
     *    if there are problems establishing a connection or accessing the database
     */
    public CoffeeDbRepository(Validator<Integer, Coffee> validator, String database) throws ValidatorException {
        super(validator, database);
    }

    /**
     * Method for creating a table in the database for coffees
     * @throws ValidatorException
     *      if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE COFFEE" +
                "(ID INTEGER PRIMARY KEY," +
                "NAME TEXT NOT NULL," +
                "ORIGIN TEXT NOT NULL," +
                "QUANTITY INTEGER," +
                "PRICE INTEGER)";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throw new DatabaseException("Problem creating COFFEE table");
        }
    }

    /**
     * Method that returns the name of the coffee table
     * @return String: the name of the table
     */
    @Override
    protected String getTableName() {
        return "COFFEE";
    }

    /**
     * Method for loading data from the coffee table from the database into the local repository
     * @throws ValidatorException
     *      if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM COFFEE";
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int quantity = resultSet.getInt(4);
                int price = resultSet.getInt(5);
                Coffee coffee = Coffee.Builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .origin(resultSet.getString(3))
                        .quantity(quantity == 0 ? null : quantity)
                        .price(price == 0 ? null : price)
                        .build();
                this.entities.putIfAbsent(coffee.getId(), coffee);
            }
        } catch (SQLException sqe) {
            throw new DatabaseException("Something went wrong while reading data from COFFEE table");
        }
    }

    /**
     * Method for executing an insert query on the COFFEE table of the database
     * @param entity : The address to be inserted
     * @throws ValidatorException
     *      if there are problems inserting data into the database
     */
    @Override
    protected void executeInsertQuery(Coffee entity) throws ValidatorException {
        String query = "INSERT INTO COFFEE (ID, NAME, ORIGIN, QUANTITY, PRICE) VALUES(?,?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getOrigin());
            setOptionalStatement(statement, 4, entity.getQuantity(), true, Types.INTEGER);
            setOptionalStatement(statement, 5, entity.getPrice(), true, Types.INTEGER);
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while inserting into COFFEE table");
        }
    }

    /**
     * Method for executing an update query on the COFFEE table of the database
     * @param entity : The entity to be updated
     * @throws ValidatorException
     *      if there are problems updating data in the database
     */
    @Override
    protected void executeUpdateQuery(Coffee entity) throws ValidatorException {
        String query = "UPDATE COFFEE SET NAME = ?, ORIGIN = ?, QUANTITY = ?," +
                "PRICE = ? WHERE ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getOrigin());
            setOptionalStatement(statement, 3, entity.getQuantity(), true, Types.INTEGER);
            setOptionalStatement(statement, 4, entity.getPrice(), true, Types.INTEGER);
            statement.setInt(5, entity.getId());
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while updating COFFEE table");
        }
    }

    /**
     * Method for executing a delete query on the COFFEE table of the database
     * @param integer : The id of the entity to be deleted
     * @throws ValidatorException
     *      if there are problems deleting data from the database
     */
    @Override
    protected void executeDeleteQuery(Integer integer) throws ValidatorException {
        String query = "DELETE FROM COFFEE WHERE ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integer);
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while deleting from COFFEE table");
        }
    }

}
