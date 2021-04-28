package repository;

import domain.Order;
import domain.Status;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderDbRepository extends DBRepo<Pair<Integer, Integer>, Order> {

    /**
     * Constructor for the DBRepoOrder classes. Either loads data from database
     * or creates a table for the order entities if it does not exist
     *
     * @param validator : a validator for the order entities
     * @param database  : the database name
     * @throws domain.Validators.ValidatorException
     *      if there was a problem connecting to the database or accessing data
     */
    public OrderDbRepository(Validator<Pair<Integer, Integer>, Order> validator, String database) {
        super(validator, database);
    }

    /**
     * Method for creating a table in the database for orders
     * @throws ValidatorException
     *   if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE ORDERS" +
                "(ORDER_ID INTEGER NOT NULL," +
                "CLIENT_ID INTEGER NOT NULL REFERENCES CLIENT(ID)," +
                "COFFEE_ID INTEGER NOT NULL REFERENCES COFFEE(ID)," +
                "STATUS TEXT," +
                "TIME TEXT," +
                "PRIMARY KEY (ORDER_ID, CLIENT_ID, COFFEE_ID))";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throw new DatabaseException("Something went wrong while creating the ORDERS table");
        }
    }

    /**
     * Method that returns the name of the order table
     * @return String: the name of the table
     */
    @Override
    protected String getTableName() {
        return "ORDERS";
    }

    /**
     * Method for loading data from the order table from the database into the local repository
     * @throws ValidatorException
     *    if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String query = "SELECT * FROM ORDERS";
        try (Connection connection = getConnection();
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String status = resultSet.getString(4);
                String time = resultSet.getString(5);
                Order order = Order.Builder()
                        .id(new ImmutablePair<>(resultSet.getInt(1), resultSet.getInt(2)))
                        .status(status == null ? null : Status.valueOf(status))
                        .time(time == null ? null : LocalDateTime.parse(time, formatter))
                        .build();

                Optional<Order> optional = this.findOne(order.getId());
                optional.ifPresentOrElse(ord -> {
                    try {
                        ord.getCoffeesId().add(resultSet.getInt(3));
                        this.entities.computeIfPresent(ord.getId(), (k, v) -> ord);
                    } catch (SQLException throwables) {
                        throw new DatabaseException(throwables.getMessage());
                    }
                }, () -> {
                    try {
                        order.getCoffeesId().add(resultSet.getInt(3));
                        this.entities.putIfAbsent(order.getId(), order);
                    } catch (SQLException throwables) {
                        throw new DatabaseException(throwables.getMessage());
                    }
                });
            }
        } catch (SQLException sqe) {
            throw new DatabaseException("Something went wrong while reading data from ORDERS table");
        }
    }

    /**
     * Method for executing an insert query on the ORDERS table of the database
     * @param entity : The address to be inserted
     * @throws ValidatorException
     *      if there are problems inserting data into the database
     */
    @Override
    protected void executeInsertQuery(Order entity) throws ValidatorException {
        addOrdersWithCoffeeIds(entity.getCoffeesId(), entity);
    }

    /**
     * Adds in the database ORDERS table multiple entries with each coffee id associated with an order
     * @param coffeeIds: the list of coffee ids
     * @param entity: the order
     * @throws ValidatorException
     *      if there was an error connecting to the database or inserting data into it
     */
    private void addOrdersWithCoffeeIds(List<Integer> coffeeIds, Order entity) throws ValidatorException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String query = "INSERT INTO ORDERS (ORDER_ID, CLIENT_ID, COFFEE_ID, STATUS, TIME) VALUES(?,?,?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            coffeeIds.forEach(id -> {
                try {
                    statement.setInt(1, entity.getId().getLeft());
                    statement.setInt(2, entity.getId().getRight());
                    statement.setInt(3, id);
                    setOptionalStatement(statement, 4, entity.getStatus() == null ? null : entity.getStatus().toString(), false, Types.BLOB);
                    setOptionalStatement(statement, 5, entity.getTime() == null ? null : entity.getTime().format(formatter), false, Types.BLOB);
                    statement.executeUpdate();
                } catch (SQLException throwables) {
                    throw new DatabaseException("Something went wrong while inserting into ORDERS table");
                }
            });
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while inserting into ORDERS table");
        }
    }

    /**
     * Returns a list of all the coffee ids associated with a saved order
     * @param entity: the order
     * @return {@code List<Integer>} list of coffee ids
     * @throws ValidatorException
     *      if there was an error connecting to the database or reading data from it
     */
    private List<Integer> getSavedCoffeeIds(Order entity) throws ValidatorException {
        List<Integer> coffeeIds = new ArrayList<>();
        String query = "SELECT COFFEE_ID FROM ORDERS WHERE ORDER_ID = ? AND CLIENT_ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, entity.getId().getLeft());
            preparedStatement.setInt(2, entity.getId().getRight());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                coffeeIds.add(resultSet.getInt(1));
            }
        } catch (SQLException throwables) {
            throw new DatabaseException("Something went wrong while reading from ORDERS table");
        }
        return coffeeIds;
    }

    /**
     * Adds coffee ids associated with an order that are in the repository but not in the database
     * @param entity: the order
     * @throws ValidatorException
     *      if there was an error inserting data into database
     */
    private void addNewCoffees(Order entity) throws ValidatorException {
        List<Integer> coffeeIds = getSavedCoffeeIds(entity);
        List<Integer> actualCoffeeIds = entity.getCoffeesId();
        List<Integer> idsNotInDb = actualCoffeeIds.stream()
                .filter(id -> !coffeeIds.contains(id))
                .collect(Collectors.toList());
        addOrdersWithCoffeeIds(idsNotInDb, entity);
    }

    /**
     * Removes from the database table ORDER multiple entries with each coffee id not associated to an order
     * @param ids: list of coffee ids
     * @param entity: the order
     * @throws ValidatorException
     *      if there was an error deleting from the database
     */
    private void removeOrdersWithCoffeeIds(List<Integer> ids, Order entity) throws ValidatorException {
        String query = "DELETE FROM ORDERS WHERE ORDER_ID = ? AND CLIENT_ID = ? AND COFFEE_ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            ids.forEach(id -> {
                try {
                    statement.setInt(1, entity.getId().getLeft());
                    statement.setInt(2, entity.getId().getRight());
                    statement.setInt(3, id);
                    statement.executeUpdate();
                } catch (SQLException throwables) {
                    throw new DatabaseException("Something went wrong while deleting from ORDERS table");
                }
            });
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while deleting from ORDERS table");
        }
    }

    /**
     * Removes entries from the ORDER table where the coffee ids are no longer associated with anything in the repository
     * @param entity: The entity to be updated
     * @throws ValidatorException
     *      if there was an error deleting from the database
     */
    private void removeOldCoffees(Order entity) throws ValidatorException {
        List<Integer> coffeeIds = getSavedCoffeeIds(entity);
        List<Integer> actualCoffeeIds = entity.getCoffeesId();
        List<Integer> idsNotInRepo = coffeeIds.stream()
                .filter(id -> !actualCoffeeIds.contains(id))
                .collect(Collectors.toList());
        removeOrdersWithCoffeeIds(idsNotInRepo, entity);
    }

    /**
     * Method for executing an update query on the CLIENT table of the database
     * @param entity : The entity to be updated
     * @throws ValidatorException
     *      if there are problems updating data in the database
     */
    @Override
    protected void executeUpdateQuery(Order entity) throws ValidatorException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        addNewCoffees(entity);
        removeOldCoffees(entity);
        String query = "UPDATE ORDERS SET STATUS = ?, TIME = ? WHERE ORDER_ID = ? AND CLIENT_ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            setOptionalStatement(statement, 1, entity.getStatus() == null ? null : entity.getStatus().toString(), false, Types.BLOB);
            setOptionalStatement(statement, 2, entity.getTime() == null ? null : entity.getTime().format(formatter), false, Types.BLOB);
            statement.setInt(3, entity.getId().getLeft());
            statement.setInt(4, entity.getId().getRight());
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while updating ORDERS table");
        }
    }

    /**
     * Method for executing a delete query on the CLIENT table of the database
     * @param integerIntegerPair : The id of the entity to be deleted
     * @throws ValidatorException
     *      if there are problems deleting data from the database
     */
    @Override
    protected void executeDeleteQuery(Pair<Integer, Integer> integerIntegerPair) throws ValidatorException {
        String query = "DELETE FROM ORDERS WHERE ORDER_ID = ? AND CLIENT_ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, integerIntegerPair.getLeft());
            statement.setInt(2, integerIntegerPair.getRight());
            statement.executeUpdate();
        } catch (SQLException se) {
            throw new DatabaseException("Something went wrong while deleting from ORDERS table");
        }
    }

}
