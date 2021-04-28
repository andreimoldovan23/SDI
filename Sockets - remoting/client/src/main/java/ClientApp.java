import services.*;
import tcp.TcpClient;
import ui.Console;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientApp {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        TcpClient tcpClient = new TcpClient();
        IClientService clientService = new ClientService(executorService, tcpClient);
        ICoffeeService coffeeService = new CoffeeService(executorService, tcpClient);
        IAddressService addressService = new AddressService(executorService, tcpClient);
        IOrderService orderService = new OrderService(executorService, tcpClient);

        Console clientConsole = new Console(clientService, coffeeService, addressService, orderService);
        clientConsole.runConsole();

        System.out.println("bye client");

        executorService.shutdown();
    }

}
