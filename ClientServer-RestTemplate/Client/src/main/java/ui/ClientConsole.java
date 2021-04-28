package ui;

import domain.Status;
import domain.Validators.Validator;
import dtos.AddressDTO;
import dtos.ClientDTO;
import dtos.CoffeeDTO;
import dtos.ShopOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;


@Component
@SuppressWarnings("DuplicatedCode")
public class ClientConsole {


    @Autowired
    private RestTemplate restTemplate;

    private final String addressesUrl = "http://localhost:8080/api/addresses";
    private final String clientsUrl = "http://localhost:8080/api/clients";
    private final String ordersUrl = "http://localhost:8080/api/orders";
    private final String coffeesUrl = "http://localhost:8080/api/coffees";

    private final Scanner scanner;

    /**
     * Constructor for Console
     */
    public ClientConsole() {

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
                 21. Filter client shopOrder
                 ---
                 24. Buy coffee for a client
                 25. Change the status of an shopOrder
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
            case "24" -> this.buyCoffee();
            case "25" -> this.changeOrderStatus();
            default -> System.out.println("Invalid command");
        }
        this.printMenu();
    }

    /**
     * Changes the status of an shopOrder
     */

    private void changeOrderStatus() {
        try {
            System.out.println("Input shopOrder ID: ");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input status: ");
            Status status = Status.valueOf(scanner.next().toLowerCase(Locale.ROOT));
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ordersUrl + "/" + orderId)
                    .queryParam("status", status);
            CompletableFuture.runAsync(() -> restTemplate.put(builder.toUriString(), ShopOrderDTO.class))
                .exceptionally(exception -> {System.out.println(exception.getMessage()); return null;});
        } catch (IllegalArgumentException ie) {
            System.out.println("Invalid input. Try again");
        }
    }

    /**
     * Reads a client and a coffee and creates an shopOrder entity
     */
    private void buyCoffee() {
        try {
            System.out.println("Input client ID: ");
            Integer clientId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input coffee ID: ");
            Integer coffeeId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            System.out.println("Input shopOrder ID: ");
            Integer orderId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ordersUrl)
                    .queryParam("clientId", clientId)
                    .queryParam("coffeeId", coffeeId)
                    .queryParam("orderId", orderId);
            CompletableFuture.runAsync(() -> restTemplate.put(builder.toUriString(), ShopOrderDTO.class))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
        } catch (NumberFormatException ne) {
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
            String finUrl = ordersUrl + "/filterClientOrders";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                    .queryParam("id", id);
            CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), ShopOrderDTO[].class))
                    .thenAccept(responseEntity -> {
                        ShopOrderDTO[] orders = responseEntity.getBody();
                        if (orders != null) {
                            Arrays.stream(orders).forEach(System.out::println);
                        }
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            String finUrl = ordersUrl + "/filterClientCoffees";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                    .queryParam("id", id);
            CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), CoffeeDTO[].class))
                    .thenAccept(responseEntity -> {
                        CoffeeDTO[] coffees = responseEntity.getBody();
                        if (coffees != null) {
                            Arrays.stream(coffees).forEach(System.out::println);
                        }
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss");
            LocalDateTime d1 = this.readDate();
            LocalDateTime d2 = this.readDate();
            String finUrl = ordersUrl + "/deleteOrdersBetweenDates";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                    .queryParam("date1", d1.format(myFormatObj))
                    .queryParam("date2", d2.format(myFormatObj));
            CompletableFuture.runAsync(() -> restTemplate.delete(builder.toUriString()))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });

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

        String finUrl = coffeesUrl + "/filterCoffeesByOrigin";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                .queryParam("origin", origin);
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), CoffeeDTO[].class))
                .thenAccept(responseEntity -> {
                    CoffeeDTO[] coffees = responseEntity.getBody();
                    if (coffees != null) {
                        Arrays.stream(coffees).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
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

            String finUrl = clientsUrl + "/filterClientsByAge";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                    .queryParam("age1", a1)
                    .queryParam("age2", a2);
            CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), ClientDTO[].class))
                    .thenAccept(response -> {
                        ClientDTO[] clients = response.getBody();
                        if (clients != null) {
                            Arrays.stream(clients).forEach(System.out::println);
                        }
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
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
            String finUrl = addressesUrl + "/deleteAddressesByNumber";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                    .queryParam("number", number);
            CompletableFuture.runAsync(() -> restTemplate.delete(builder.toUriString()))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
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
            ClientDTO client = this.readClient();
            CompletableFuture.runAsync(() -> restTemplate.postForObject(clientsUrl, client, ClientDTO.class))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            String finUrl = clientsUrl + "/" + id;
            CompletableFuture.runAsync(() -> restTemplate.delete(finUrl))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            ClientDTO client = this.readClient();
            String finUrl = clientsUrl + "/" + client.getId();
            CompletableFuture.runAsync(() -> restTemplate.put(finUrl, client))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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

        String finUrl = clientsUrl + "/filterClients";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                .queryParam("name", name);
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), ClientDTO[].class))
                .thenAccept(response -> {
                    ClientDTO[] clients = response.getBody();
                    if (clients != null) {
                        Arrays.stream(clients).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     * Adds a coffee
     */
    private void addCoffee() {
        System.out.println("----- Add Coffee -----");
        try {
            CoffeeDTO coffee = this.readCoffee();
            CompletableFuture.runAsync(() -> restTemplate.postForObject(coffeesUrl, coffee, CoffeeDTO.class))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            String finUrl = coffeesUrl + "/" + id;
            CompletableFuture.runAsync(() -> restTemplate.delete(finUrl))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            CoffeeDTO coffee = this.readCoffee();
            String finUrl = coffeesUrl + "/" + coffee.getId();
            CompletableFuture.runAsync(() -> restTemplate.put(finUrl, coffee))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            AddressDTO address = this.readAddress();
            CompletableFuture.runAsync(() -> restTemplate.postForObject(addressesUrl, address, AddressDTO.class))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            String finUrl = addressesUrl + "/" + id;
            CompletableFuture.runAsync(() -> restTemplate.delete(finUrl))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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
            AddressDTO address = this.readAddress();
            String finUrl = addressesUrl + "/" + address.getId();
            CompletableFuture.runAsync(() -> restTemplate.put(finUrl, address))
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
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

        String finUrl = coffeesUrl + "/filterCoffees";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(finUrl)
                .queryParam("name", name);
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(builder.toUriString(), CoffeeDTO[].class))
                .thenAccept(response -> {
                    CoffeeDTO[] coffees = response.getBody();
                    if (coffees != null) {
                        Arrays.stream(coffees).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     * Prints the clients
     */
    private void printClients() {
        System.out.println("Printing clients:");
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(clientsUrl, ClientDTO[].class))
                .thenAccept(response -> {
                    ClientDTO[] clients= response.getBody();
                    if (clients != null) {
                        Arrays.stream(clients).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     * Prints the coffees
     */
    private void printCoffees() {
        System.out.println("Printing coffees:");
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(coffeesUrl, CoffeeDTO[].class))
                .thenAccept(response -> {
                    CoffeeDTO[] coffees = response.getBody();
                    if (coffees != null) {
                        Arrays.stream(coffees).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     *Prints all addresses
     */
    private void printAddresses() {
        System.out.println("Printing addresses:");
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(addressesUrl, AddressDTO[].class))
                .thenAccept(response -> {
                    AddressDTO[] addresses = response.getBody();
                    if (addresses != null) {
                        Arrays.stream(addresses).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     *Prints all orders
     */
    private void printOrders() {
        System.out.println("Printing orders:");
        CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(ordersUrl, ShopOrderDTO[].class))
                .thenAccept(response -> {
                    ShopOrderDTO[] orders = response.getBody();
                    if (orders != null) {
                        Arrays.stream(orders).forEach(System.out::println);
                    }
                })
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    /**
     * Functions that reads a client from console input
     * @return client
     */
    private ClientDTO readClient() {
        Integer id;
        String firstName;
        String lastName;
        Integer age;
        String phoneNumber;
        Integer addressId;

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
        addressId = (Integer) Validator.checkInput(scanner.nextLine(), "", true);

        String finUrl = addressesUrl + "/" + addressId;
        ResponseEntity<AddressDTO> response = restTemplate.getForEntity(finUrl, AddressDTO.class);
        AddressDTO address = response.getBody();

        return new ClientDTO(id, lastName, firstName, age, phoneNumber, address);
    }

    /**
     * Function that reads a coffee from console
     * @return coffee
     */
    private CoffeeDTO readCoffee() {
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

        return new CoffeeDTO(id, name, origin, quantity, price);

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
    private AddressDTO readAddress() {
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

        return new AddressDTO(id, city, street, number);
    }

}
