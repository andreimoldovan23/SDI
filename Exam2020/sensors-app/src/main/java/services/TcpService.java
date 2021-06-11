package services;

public interface TcpService {
    String HOST = "localhost";
    Integer PORT = 12000;

    void generateMeasurements(Integer id, String name, Integer lowerBound, Integer upperBound);
}
