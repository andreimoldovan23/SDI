package services;

import domain.SensorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tcp.TcpClient;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TcpServiceImpl implements TcpService {
    private final TcpClient tcpClient;

    @Override
    public void generateMeasurements(Integer id, String name, Integer lowerBound, Integer upperBound) {
        log.trace("Received data from console: {}, {}, {}, {}", id, name, lowerBound, upperBound);
        SensorProperties properties = new SensorProperties(id, name, lowerBound, upperBound);
        try {
            tcpClient.run(properties);
        } catch (IOException ioe) {
            log.trace("Exception occurred while communicating with the server: {}", ioe.getMessage());
        }
    }
}
