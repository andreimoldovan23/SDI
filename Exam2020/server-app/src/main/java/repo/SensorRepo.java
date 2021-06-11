package repo;

import domain.SensorInfo;

import java.sql.Connection;

public interface SensorRepo {
    void createTable();
    void dropTable();
    void insert(SensorInfo sensorInfo);
    Connection getConnection();
}
