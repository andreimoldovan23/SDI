package repo;

import domain.SensorInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class SensorRepoImpl implements SensorRepo {
    private final String database;
    private final String user;
    private final String password;

    public SensorRepoImpl(String database, String user, String password) {
        this.database = "jdbc:mysql:" + database;
        this.user = user;
        this.password = password;
        dropTable();
        createTable();
    }

    @Override
    public void createTable() {
        log.trace("creating table");
        String query = "CREATE TABLE SENSOR" +
                "(ID INT NOT NULL AUTO_INCREMENT," +
                "NAME VARCHAR(50) NOT NULL," +
                "MEASUREMENT INT NOT NULL," +
                "TIME TIMESTAMP NOT NULL," +
                "PRIMARY KEY(ID))";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            log.trace("Table already exists");
        } catch (RuntimeException e) {
            log.trace("{}", e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        log.trace("dropping table");
        String query = "DROP TABLE SENSOR";
        try(Connection connection = getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            log.trace("Table already dropped");
        } catch (RuntimeException e) {
            log.trace("{}", e.getMessage());
        }
    }

    @Override
    public void insert(SensorInfo sensorInfo) {
        log.trace("Inserting: {}", sensorInfo);
        String query = "INSERT INTO SENSOR (NAME, MEASUREMENT, TIME) VALUES (?,?,?)";
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, sensorInfo.getName());
            statement.setInt(2, sensorInfo.getMeasurement());
            statement.setObject(3, sensorInfo.getTime(), Types.TIMESTAMP);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.trace("Error inserting into table");
        } catch (RuntimeException e) {
            log.trace("{}", e.getMessage());
        }
    }

    @Override
    public Connection getConnection() {
        log.trace("Getting connection");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(database, user, password);
        } catch (Exception e) {
            log.trace("error while connecting to database");
            throw new RuntimeException(e);
        }
    }
}
