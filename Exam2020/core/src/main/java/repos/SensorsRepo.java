package repos;

import domain.SensorMeasurement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorsRepo extends JpaRepository<SensorMeasurement, Integer> {

    @Query("select distinct s.name from SensorMeasurement s")
    List<String> getAll();

    @Query("select s from SensorMeasurement s where s.name = ?1 order by s.time DESC")
    List<SensorMeasurement> getFirstFourByName(String name, Pageable pageable);
}
