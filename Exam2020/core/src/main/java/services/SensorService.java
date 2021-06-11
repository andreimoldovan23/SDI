package services;

import domain.SensorMeasurement;

import java.util.List;

public interface SensorService {
    List<String> getSensorNames();
    List<SensorMeasurement> getFirstFour(String name);
}
