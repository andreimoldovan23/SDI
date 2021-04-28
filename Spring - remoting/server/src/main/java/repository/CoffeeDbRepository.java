package repository;

import domain.Coffee;
import domain.Validators.DatabaseException;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

public class CoffeeDbRepository extends DBRepo<Integer, Coffee> {

    /**
     * Constructor for the DBRepoCoffee class. Either loads data from database
     * or creates a table for the coffee entities if it does not exist
     *
     * @param validator : a validator for the coffee entities
     * @throws ValidatorException
     *    if there are problems establishing a connection or accessing the database
     */
    public CoffeeDbRepository(Validator<Integer, Coffee> validator, JdbcOperations jdbcOperations) throws ValidatorException {
        super(validator, jdbcOperations);
    }

    /**
     * Method for creating a table in the database for coffees
     * @throws ValidatorException
     *      if there are problems creating a table in the database
     */
    @Override
    protected void createTable() throws ValidatorException {
        String query = "CREATE TABLE IF NOT EXISTS COFFEE" +
                "(ID INTEGER PRIMARY KEY," +
                "NAME TEXT NOT NULL," +
                "ORIGIN TEXT NOT NULL," +
                "QUANTITY INTEGER," +
                "PRICE INTEGER)";
        try {
            jdbcOperations.execute(query);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Problem creating COFFEE table");
        }
    }

    /**
     * Drops the COFFEE table from the database
     */
    @Override
    public void deleteTable() {
        String query = "DELETE FROM COFFEE";
        jdbcOperations.update(query);
        this.entities.clear();
    }

    /**
     * Method for loading data from the coffee table from the database into the local repository
     * @throws ValidatorException
     *      if there are problems reading data
     */
    @Override
    protected void loadData() throws ValidatorException {
        String query = "SELECT * FROM COFFEE";
        try {
            List<Coffee> coffees = jdbcOperations.query(query, (rs, rowCount) -> {
                int quantity = rs.getInt("QUANTITY");
                int price = rs.getInt("PRICE");
                return Coffee.Builder()
                        .id(rs.getInt("ID"))
                        .name(rs.getString("NAME"))
                        .origin(rs.getString("ORIGIN"))
                        .quantity(quantity == 0 ? null : quantity)
                        .price(price == 0 ? null : price)
                        .build();
            });
            coffees.forEach(coffee -> this.entities.putIfAbsent(coffee.getId(), coffee));
        } catch (DataAccessException ex) {
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
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getId(), entity.getName(), entity.getOrigin(),
                    entity.getQuantity(), entity.getPrice());
        } catch (DataAccessException ex) {
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
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, entity.getName(), entity.getOrigin(), entity.getQuantity(),
                    entity.getPrice(), entity.getId());
        } catch (DataAccessException ex) {
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
        try {
            jdbcOperations.execute(activateConstraints);
            jdbcOperations.update(query, integer);
        } catch (DataAccessException ex) {
            throw new DatabaseException("Something went wrong while deleting from COFFEE table");
        }
    }

}
