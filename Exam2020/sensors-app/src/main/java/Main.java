import console.Console;
import services.TcpService;
import services.TcpServiceImpl;
import tcp.TcpClient;
import tcp.TcpHelper;

public class Main {
    public static void main(String[] args) {
        TcpHelper tcpHelper = new TcpHelper();
        TcpClient tcpClient = new TcpClient(tcpHelper);
        TcpService tcpService = new TcpServiceImpl(tcpClient);
        Console console = new Console(tcpService);
        console.run();
    }
}
