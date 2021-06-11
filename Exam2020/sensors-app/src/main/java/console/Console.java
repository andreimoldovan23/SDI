package console;

import lombok.RequiredArgsConstructor;
import services.TcpService;

import java.util.Scanner;

@RequiredArgsConstructor
public class Console {

    private final TcpService tcpService;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input id: ");
        Integer id = Integer.parseInt(scanner.nextLine());
        System.out.println("Input name: ");
        String name = scanner.nextLine();
        System.out.println("Input lower bound: ");
        Integer lowerBound = Integer.parseInt(scanner.nextLine());
        System.out.println("Input upper bound: ");
        Integer upperBound = Integer.parseInt(scanner.nextLine());
        tcpService.generateMeasurements(id, name, lowerBound, upperBound);
    }

}
