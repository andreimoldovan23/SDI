package ui;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.Status;
import domain.Validators.Validator;
import services.IAddressService;
import services.IClientService;
import services.ICoffeeService;
import services.IOrderService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Console Interface
 */
@SuppressWarnings("DuplicatedCode")
public class Console {

    private final IClientService clientService;
    private final ICoffeeService coffeeService;
    private final IAddressService addressService;
    private final IOrderService orderService;
    private final Scanner scanner;

    /**
     * Constructor for Console
     */
    public Console(IAddressService addressService, IClientService clientService, ICoffeeService coffeeService,
                   IOrderService orderService) {
        this.addressService = addressService;
        this.clientService = clientService;
        this.coffeeService = coffeeService;
        this.orderService = orderService;
        scanner = new Scanner(System.in);
    }

    /**
     * Function that prints a menu
     */
    private void printMenu() {
        System.out.println("""
    
                -------Coffee Shop------
                 1. Add client
                 2. Delete client
                 3. Update client
                 4. Filter clients by name
                 ---
                 5. Add coffee
                 6. Delete coffee
                 7. Update coffee
                 8. Filter coffees by name
                 ---
                 9. Add address
                 10. Delete address
                 11. Update Address
                 ---
                 12. Print clients
                 13. Print coffees
                 14. Print addresses
                 15. Print orders
                 ---
                 16. Delete all orders between two given dates
                 17. List all coffees with the origin containing a given string
                 18. List all clients whose age is between two given values
                 19. Delete all addresses with a given number
                 ---
                 20. Filter client coffees
                 21. Filter client order
                 22. Add coffees to order
                 23. Delete coffees from order
                 ---
                 24. Buy coffee for a client
                 25. Change the status of an order
                 0. Exit
                 -----------------------
                """);
    }

    /**
     * This is the run method used at the start of the Application
     *
     */
    public void runConsole() {
        this.printMenu();
        Stream.generate(scanner::nextLine)
                .limit(10000)
                .forEach(this::commandSwitcher);
    }

    /**
     * Function that executes a command
     * @param command String - the command to be executed
     */
    private void commandSwitcher(String command) {
        switch (command) {
            case "0" -> {
                System.out.println("-----------------------");
                scanner.close();
                System.exit(0);
            }
            case "1"  -> this.addClient();
            case "2"  -> this.deleteClient();
            case "3"  -> this.updateClient();
            case "4"  -> this.filterClients();
            case "5"  -> this.addCoffee();
            case "6"  -> this.deleteCoffee();
            case "7"  -> this.updateCoffee();
            case "8"  -> this.filterCoffees();
            case "9" -> this.addAddress();
            case "10" -> this.deleteAddress();
            case "11" -> this.updateAddress();
            case "12"  -> this.printClients();
            case "13" -> this.printCoffees();
            case "14" -> this.printAddresses();
            case "15" -> this.printOrders();
            case "16" -> this.deleteOrdersBetweenDates();
            case "17" -> this.filterCoffeesByOrigin();
            case "18" -> this.filterClientsByAge();
            case "19" -> this.deleteAddressesByNumber();
            case "20" -> this.filterClientCoffees();
            case "21" -> this.filterClientOrders();
            case "22" -> this.addCoffeesToOrder();
            case "23" -> this.deleteCoffeesFromOrder() ;
            case "24" -> this.buyCoffee();
            case "25" -> this.changeOrderStatus();
            default -> System.out.println("Invalid command");
        }
        this.printMenu();
    }

    /**
     * Changes the status of an order
     */
    private void changeOrderStatus() {
        try {
            System.out.println("Input client ID: ");
            Integer clientId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input order ID: ");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input status: ");
            Status status = Status.valueOf(scanner.next().toLowerCase(Locale.ROOT));
            CompletableFuture.runAsync(() -> orderService.changeStatus(orderId, clientId, status))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (IllegalArgumentException ie) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Reads a client and a coffee and creates an order entity
     */
    private void buyCoffee() {
        try {
            System.out.println("Input client ID: ");
            Integer clientId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input coffee ID: ");
            Integer coffeeId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input order ID: ");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            CompletableFuture.runAsync(() -> orderService.buyCoffee(orderId, clientId, coffeeId))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException ne) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     *  Returns the list of coffee ids
     */
    private List<Integer> GetCoffeeIds () {
        List<Integer> coffeeIds = new ArrayList<>();
        System.out.println("Input Number of Coffees:\n>>>");
        int no_of_coffees = Integer.parseInt(scanner.nextLine());
        System.out.println("Input coffee id: ");
        Stream.generate(() -> {
            System.out.println("Input coffee id: ");
            return scanner.nextLine();
        })
                .limit(no_of_coffees)
                .map(Integer::parseInt)
                .forEach(coffeeIds::add);
        return coffeeIds;
    }

    /**
     *  Deletes coffees from a given order
     */
    @SuppressWarnings("DuplicatedCode")
    private void deleteCoffeesFromOrder() {
        try {
            System.out.println("Input Client Id:\n>>>");
            Integer clientId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input Order Id:\n>>>");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            List<Integer> coffeeIds = GetCoffeeIds();
            CompletableFuture.runAsync(() -> orderService.deleteCoffeesFromOrder(orderId, clientId, coffeeIds))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     *  Adds coffees to a given order
     */
    @SuppressWarnings("DuplicatedCode")
    private void addCoffeesToOrder() {
        try {
            System.out.println("Input Client Id:\n>>>");
            Integer clientId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input Order Id:\n>>>");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            List<Integer> coffeeIds = GetCoffeeIds();
            CompletableFuture.runAsync(() -> orderService.addCoffeesToOrder(orderId, clientId, coffeeIds))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     *  Filters the orders of a given client
     */
    private void filterClientOrders() {
        try {
            System.out.println("Filtering orders:");

            System.out.println("Input Client Id:\n>>>");
            Integer id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

            CompletableFuture.supplyAsync(() -> orderService.filterClientOrders(id))
                    .thenAccept(orders -> orders.forEach(System.out::println));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     *  Filters the coffees of a given client
     */
    private void filterClientCoffees() {
        try {
            System.out.println("Filtering orders:");

            System.out.println("Input Client Id:\n>>>");
            Integer id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

            CompletableFuture.supplyAsync(() -> orderService.filterClientCoffees(id))
                    .thenAccept(coffees -> coffees.forEach(System.out::println));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Deletes all orders with the timestamps in a given interval
     */
    private void deleteOrdersBetweenDates() {
        System.out.println("Deleting Orders");

        try {
            LocalDateTime d1 = this.readDate();
            LocalDateTime d2 = this.readDate();

            CompletableFuture.runAsync(() -> orderService.deleteOrderByDate(d1, d2));
        } catch (NumberFormatException ne) {
            System.out.println("Invalid input");
        }
    }

    /**
     * Lists all coffees with a substring in their origin
     */
    private void filterCoffeesByOrigin() {
        System.out.println("Filtering coffees:");

        System.out.println("Input Coffee Origin:\n>>>");
        String origin = scanner.nextLine();

        CompletableFuture.supplyAsync(() -> coffeeService.filterCoffeesByOrigin(origin))
                .thenAccept(coffees -> coffees.forEach(System.out::println));
    }

    /**
     * Lists all the client with the ages in a given interval
     */
    private void filterClientsByAge() {
        System.out.println("Filtering Clients");

        System.out.println("Input first age:\n>>>");
        Integer a1;
        Integer a2;
        try {
            a1 = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input second age:\n>>>");
            a2 = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

            Optional.ofNullable(a1).flatMap(opt -> Optional.ofNullable(a2))
                    .ifPresentOrElse(opt2 -> CompletableFuture.supplyAsync(() ->
                            clientService.filterClientsInAgeInterval(a1, a2))
                            .thenAccept(clients -> clients.forEach(System.out::println)), () -> {
                        throw new NumberFormatException();
                    });
        } catch (NumberFormatException ne) {
            System.out.println("Invalid input");
        }
    }

    /**
     * Deletes an address with a given number
     */
    private void deleteAddressesByNumber() {
        System.out.println("Deleting Addresses");

        System.out.println("Input Address Number:\n>>>");
        Integer number;
        try {
            number = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            Optional.ofNullable(number).ifPresentOrElse(opt -> CompletableFuture.runAsync(() ->
                    addressService.deleteAddressWithNumber(number)), () -> {
                    throw new NumberFormatException();
            });
        } catch (NumberFormatException ne) {
            System.out.println("Invalid input");
        }
    }

    /**
     * Function that adds a client
     */
    private void addClient() {
        System.out.println("----- Add client -----");

        try {
            Client client = this.readClient();
            CompletableFuture.runAsync(() -> clientService.Add(client))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Deletes a client
     */
    private void deleteClient() {
        System.out.println("----- Delete Client -----");
        Integer id;
        try {
            System.out.println("Input ID:\n>>>");
            id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            CompletableFuture.runAsync(() -> clientService.Delete(id))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Updates a client
     */
    private void updateClient() {
        System.out.println("----- Update Client -----");

        try {
            Client client = this.readClient();
            CompletableFuture.runAsync(() -> clientService.Update(client))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Filters clients and prints them
     */
    private void filterClients() {
        System.out.println("Filtering clients:");

        System.out.println("Input Client Name (First Name or Last Name):\n>>>");
        String name = scanner.nextLine();

        CompletableFuture.supplyAsync(() -> clientService.filterClientsByName(name))
                .thenAccept(clients -> clients.forEach(System.out::println));
    }

    /**
     * Adds a coffee
     */
    private void addCoffee() {
        System.out.println("----- Add Coffee -----");
        try {
            Coffee coffee = this.readCoffee();
            CompletableFuture.runAsync(() -> coffeeService.Add(coffee))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }

    }

    /**
     * Deletes a coffee
     */
    private void deleteCoffee() {
        System.out.println("----- Delete Coffee -----");
        Integer id;
        try {
            System.out.println("Input ID:\n>>>");
            id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            CompletableFuture.runAsync(() -> coffeeService.Delete(id))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Updates a coffee
     */
    private void updateCoffee() {
        System.out.println("----- Update Coffee -----");

        try {
            Coffee coffee = this.readCoffee();
            CompletableFuture.runAsync(() -> coffeeService.Update(coffee))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Adds an address
     */
    private void addAddress() {
        System.out.println("----- Add Address -----");
        try {
            Address address = this.readAddress();
            CompletableFuture.runAsync(() -> addressService.Add(address))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Deletes an address
     */
    private void deleteAddress() {
        System.out.println("----- Delete Address -----");
        Integer id;
        try {
            System.out.println("Input ID:\n>>>");
            id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            CompletableFuture.runAsync(() -> addressService.Delete(id))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Updates an address
     */
    private void updateAddress() {
        System.out.println("----- Update Address -----");
        try {
            Address address = this.readAddress();
            CompletableFuture.runAsync(() -> addressService.Update(address))
                    .exceptionally(ex -> { System.out.println(ex.toString()); return null; });
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Filter coffees by name
     */
    private void filterCoffees() {
        System.out.println("Filtering coffees:");

        System.out.println("Input Coffee Name:\n>>>");
        String name = scanner.nextLine();

        CompletableFuture.supplyAsync(() -> coffeeService.filterCoffeesByName(name))
                .thenAccept(clients -> clients.forEach(System.out::println));
    }

    /**
     * Prints the clients
     */
    private void printClients() {
        System.out.println("Printing clients:");
        CompletableFuture.supplyAsync(clientService::getAll)
                .thenAccept(clients -> clients.forEach(System.out::println));
    }

    /**
     * Prints the coffees
     */
    private void printCoffees() {
        System.out.println("Printing coffees:");
        CompletableFuture.supplyAsync(coffeeService::getAll)
                .thenAccept(coffees -> coffees.forEach(System.out::println));
    }

    /**
     *Prints all addresses
     */
    private void printAddresses() {
        System.out.println("Printing addresses:");
        CompletableFuture.supplyAsync(addressService::getAll)
                .thenAccept(addresses -> addresses.forEach(System.out::println));
    }

    /**
     *Prints all orders
     */
    private void printOrders() {
        System.out.println("Printing orders:");
        CompletableFuture.supplyAsync(orderService::getAll)
                .thenAccept(orders -> orders.forEach(System.out::println));
    }

    /**
     * Functions that reads a client from console input
     * @return client
     */
    private Client readClient() {
        Integer id;
        String firstName;
        String lastName;
        Integer age;
        String phoneNumber;
        Integer address;

        System.out.println("Input ID:\n>>>");
        id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
        System.out.println("Input First Name:\n>>>");
        firstName = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Last Name:\n>>>");
        lastName = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Age:\n>>>");
        age = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
        System.out.println("Input Phone Number:\n>>>");
        phoneNumber = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input address id:\n>>>");
        address = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

        return Client.Builder()
                .id(id)
                .lastName(lastName)
                .firstName(firstName)
                .age(age)
                .phoneNumber(phoneNumber)
                .addressId(address)
                .build();
    }

    /**
     * Function that reads a coffee from console
     * @return coffee
     */
    private Coffee readCoffee() {
        Integer id;
        String name;
        String origin;
        Integer quantity;
        Integer price;

        System.out.println("Input ID:\n>>>");
        id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
        System.out.println("Input Name:\n>>>");
        name = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Origin:\n>>>");
        origin = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Quantity:\n>>>");
        quantity = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
        System.out.println("Input Price:\n>>>");
        price = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

        return Coffee.Builder()
                .id(id)
                .name(name)
                .origin(origin)
                .quantity(quantity)
                .price(price)
                .build();

    }

    /**
     * Reads a timestamp from the keyboard
     * @return LocalDateTime
     */
    private LocalDateTime readDate() throws NumberFormatException {
        int year, month, day, hour, minute, seconds;

        System.out.println("Input Year:\n>>>");
        year = Integer.parseInt(scanner.nextLine());
        System.out.println("Input Month:\n>>>");
        month = Integer.parseInt(scanner.nextLine());
        System.out.println("Input Day:\n>>>");
        day = Integer.parseInt(scanner.nextLine());
        System.out.println("Input Hour:\n>>>");
        hour = Integer.parseInt(scanner.nextLine());
        System.out.println("Input Minute:\n>>>");
        minute = Integer.parseInt(scanner.nextLine());
        System.out.println("Input Second:\n>>>");
        seconds = Integer.parseInt(scanner.nextLine());

        return LocalDateTime.of(year, month, day, hour, minute, seconds);
    }

    /**
     * Reads an address from the keyboard
     * @return address
     */
    private Address readAddress() {
        Integer id;
        String city;
        String street;
        Integer number;

        System.out.println("Input ID:\n>>>");
        id = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
        System.out.println("Input City:\n>>>");
        city = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Street:\n>>>");
        street = (String) Validator.checkInput(scanner.nextLine(), "", false);
        System.out.println("Input Number:\n>>>");
        number = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

        return Address.Builder()
                .id(id)
                .city(city)
                .street(street)
                .number(number)
                .build();
    }

}
