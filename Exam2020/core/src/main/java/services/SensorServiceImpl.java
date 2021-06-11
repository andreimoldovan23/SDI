package services;

import domain.SensorMeasurement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import repos.SensorsRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorServiceImpl implements SensorService {
    private final SensorsRepo sensorsRepo;

    @Override
    public List<String> getSensorNames() {
        log.trace("getting all the distinct sensor names");
        return sensorsRepo.getAll();
    }

    @Override
    public List<SensorMeasurement> getFirstFour(String name) {
        log.trace("getting latest four measurements for: {}", name);
        return sensorsRepo.getFirstFourByName(name, PageRequest.of(0, 4));
    }
}
