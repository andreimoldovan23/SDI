package controllers;

import dtos.SensorDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mappers.SensorMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import services.SensorService;
import tcp.TcpWeb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SensorController {

    private final SensorMapper sensorMapper;
    private final SensorService sensorService;
    private final TcpWeb tcpWeb;

    @GetMapping("/sensors")
    public DtosList getSensorNames() {
        DtosList list = new DtosList();
        list.setMeasurements(sensorService.getSensorNames().stream()
                .map(name -> {
                    SensorDTO dto = new SensorDTO();
                    dto.setName(name);
                    return dto;
                })
                .collect(Collectors.toList()));
        log.trace("got names: {}", list.getMeasurements());
        return list;
    }

    @GetMapping("/sensors/{name}")
    public DtosList getSensorDetails(@PathVariable String name) {
        DtosList list = new DtosList();
        list.setMeasurements(sensorService.getFirstFour(name).stream()
                .map(sensorMapper::entityToDTO)
                .collect(Collectors.toList()));
        log.trace("got measurements: {}", list.getMeasurements());
        return list;
    }

    @PostMapping("/sensors/{name}/kill")
    public void killSensor(@PathVariable String name) {
        try {
            tcpWeb.kill(name);
        } catch (IOException ioe) {
            log.trace("Couldn't kill sensor");
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class DtosList {
        private List<SensorDTO> measurements = new ArrayList<>();
    }
}
