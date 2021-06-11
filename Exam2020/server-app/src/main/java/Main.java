import repo.SensorRepo;
import repo.SensorRepoImpl;
import service.TcpService;
import tcp.TcpHelper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String database = "//localhost:9999/sensors";
        String user = "root";
        String password = "andrei2301?";
        TcpHelper tcpHelper = new TcpHelper();
        SensorRepo sensorRepo = new SensorRepoImpl(database, user, password);
        TcpService tcpService = new TcpService(sensorRepo, tcpHelper);
        try {
            tcpService.startServer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
