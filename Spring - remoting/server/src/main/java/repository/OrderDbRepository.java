package repository;

import domain.Order;
import domain.Status;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.SQLException;
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
     * @throws domain.Validators.ValidatorException
     *      if there was a problem connecting to the database or accessing data
     */
    public OrderDbRepository(Validator<Pair<Integer, Integer>, Order> validator, JdbcOperations jdbcOperations) {
        super(validator, jdbcOperations);
    }

    /**
     * Method for creating a table in the database for orders
     * @throws ValidatorException
     *   if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE IF NOT EXISTS ORDERS" +
                "(ORDER_ID INTEGER NOT NULL," +
                "CLIENT_ID INTEGER NOT NULL REFERENCES CLIENT(ID)," +
                "COFFEE_ID INTEGER NOT NULL REFERENCES COFFEE(ID)," +
                "STATUS TEXT," +
                "TIME TEXT," +
                "PRIMARY KEY (ORDER_ID, CLIENT_ID, COFFEE_ID))";
        try {
            jdbcOperations.execute(query);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while creating the ORDERS table");
        }
    }

    /**
     * Drops the ORDERS table from the database
     */
    @Override
    public void deleteTable() {
        String query = "DELETE FROM ORDERS";
        jdbcOperations.update(query);
        this.entities.clear();
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
        try {
            jdbcOperations.query(query, (resultSet, rowCount) -> {
                String status = resultSet.getString("STATUS");
                String time = resultSet.getString("TIME");
                Order order = Order.Builder()
                        .id(new ImmutablePair<>(resultSet.getInt("ORDER_ID"),
                                resultSet.getInt("CLIENT_ID")))
                        .status(status == null ? null : Status.valueOf(status))
                        .time(time == null ? null : LocalDateTime.parse(time, formatter))
                        .build();

                Optional<Order> optional = this.findOne(order.getId());
                optional.ifPresentOrElse(ord -> {
                    try {
                        ord.getCoffeesId().add(resultSet.getInt("COFFEE_ID"));
                        this.entities.computeIfPresent(ord.getId(), (k, v) -> ord);
                    } catch (SQLException throwables) {
                        throw new DatabaseException(throwables.getMessage());
                    }
                }, () -> {
                    try {
                        order.getCoffeesId().add(resultSet.getInt("COFFEE_ID"));
                        this.entities.putIfAbsent(order.getId(), order);
                    } catch (SQLException throwables) {
                        throw new DatabaseException(throwables.getMessage());
                    }
                });
                return order;
            });
        } catch (DataAccessException ex) {
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
        coffeeIds.forEach(id -> {
                try {
                    String status = entity.getStatus() == null ? null : entity.getStatus().toString();
                    String localDateTime = entity.getTime() == null ? null : entity.getTime().format(formatter);
                    jdbcOperations.execute(activateConstraints);
                    jdbcOperations.update(query, entity.getId().getLeft(), entity.getId().getRight(),
                            id, status, localDateTime);
                } catch (DataAccessException ex) {
                    throw new DatabaseException("Something went wrong while inserting into ORDERS table");
                }
            });
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
        try {
            jdbcOperations.query(query, new Object[] {entity.getId().getLeft(), entity.getId().getRight()},
                    (rs, rowCount) -> {
                        Integer id = rs.getInt("COFFEE_ID");
                        coffeeIds.add(id);
                        return id;
                    });
        } catch (DataAccessException ex) {
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
        ids.forEach(id -> {
                try {
                    jdbcOperations.execute(activateConstraints);
                    jdbcOperations.update(query, entity.getId().getLeft(), entity.getId().getRight(), id);
                } catch (DataAccessException ex) {
                    throw new DatabaseException("Something went wrong while deleting from ORDERS table");
                }
            });
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
        try {
            String status = entity.getStatus() == null ? null : entity.getStatus().toString();
            String time = entity.getTime() == null ? null : entity.getTime().format(formatter);
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, status, time, entity.getId().getLeft(), entity.getId().getRight());
        } catch (DataAccessException ex) {
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
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, integerIntegerPair.getLeft(), integerIntegerPair.getRight());
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while deleting from ORDERS table");
        }
    }

}
