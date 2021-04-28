package service;

import domain.*;
import domain.Validators.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import services.IOrderService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {
    private static final String dbFilename = System.getenv("TESTDB");

    private static Coffee coffee1, coffee2, coffee3;
    private static Order order1;

    private static Repository<Integer, Client> clientDbRepository;
    private static Repository<Integer, Coffee> coffeeDbRepository;
    private static Repository<Pair<Integer, Integer>, Order> orderDbRepository;

    private static IOrderService orderService;

    @BeforeEach
    public void setUp() {
        Address address1 = Address.Builder()
                .id(1)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Address address2 = Address.Builder()
                .id(2)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Address address3 = Address.Builder()
                .id(3)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Client client1 = Client.Builder()
                .id(1)
                .firstName("first")
                .lastName("last")
                .addressId(1)
                .build();
        Client client2 = Client.Builder()
                .id(2)
                .firstName("first")
                .lastName("last")
                .addressId(2)
                .build();
        Client client3 = Client.Builder()
                .id(3)
                .firstName("first")
                .lastName("last")
                .addressId(3)
                .build();
        coffee1 = Coffee.Builder()
                .id(1)
                .name("Jacobs")
                .origin("Colombia")
                .price(12)
                .build();
        coffee2 = Coffee.Builder()
                .id(2)
                .name("Nescafe")
                .origin("Jamaica")
                .price(14)
                .build();
        coffee3 = Coffee.Builder()
                .id(3)
                .name("HeavyCup")
                .origin("Lebanon")
                .price(15)
                .build();
        order1 = Order.Builder()
                .id(new ImmutablePair<>(1, 1))
                .status(Status.delivered)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order1.getCoffeesId().add(1);
        order1.getCoffeesId().add(2);
        order1.getCoffeesId().add(3);
        Order order2 = Order.Builder()
                .id(new ImmutablePair<>(2, 2))
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        order2.getCoffeesId().add(2);
        order2.getCoffeesId().add(3);
        Order order3 = Order.Builder()
                .id(new ImmutablePair<>(3, 3))
                .status(Status.pending)
                .time(LocalDateTime.of(2020, 3, 4, 0, 0))
                .build();
        order3.getCoffeesId().add(3);
        try {
            AddressDbRepository addressDbRepository = new AddressDbRepository(new AddressValidator(), dbFilename);
            addressDbRepository.save(address1);
            addressDbRepository.save(address2);
            addressDbRepository.save(address3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try{
            clientDbRepository = new ClientDbRepository(new ClientValidator(), dbFilename);
            clientDbRepository.save(client1);
            clientDbRepository.save(client2);
            clientDbRepository.save(client3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try{
            coffeeDbRepository = new CoffeeDbRepository(new CoffeeValidator(), dbFilename);
            coffeeDbRepository.save(coffee1);
            coffeeDbRepository.save(coffee2);
            coffeeDbRepository.save(coffee3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try{
            orderDbRepository = new OrderDbRepository(new OrderValidator(), dbFilename);
            orderDbRepository.save(order1);
            orderDbRepository.save(order2);
            orderDbRepository.save(order3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        Connection conn;
        String db = "jdbc:sqlite:" + dbFilename;
        String dropAddress = "DROP TABLE ADDRESS";
        String dropClient = "DROP TABLE CLIENT";
        String dropCoffee = "DROP TABLE COFFEE";
        String dropOrder = "DROP TABLE ORDERS";

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(db);
            conn.createStatement().executeUpdate("PRAGMA foreign_keys= ON");
            conn.createStatement().execute(dropOrder);
            conn.createStatement().execute(dropCoffee);
            conn.createStatement().execute(dropClient);
            conn.createStatement().execute(dropAddress);
        } catch (Exception e) {
            throw new DatabaseException("Something went wrong while connecting");
        }
    }

    @Test
    void filterClientCoffees() throws ExecutionException, InterruptedException {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        Set<Coffee> coffees = orderService.filterClientCoffees(1).get();
        assertTrue(coffees.contains(coffee1));
        assertTrue(coffees.contains(coffee2));
        assertTrue(coffees.contains(coffee3));
    }

    @Test
    void filterClientOrders() throws ExecutionException, InterruptedException {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        Set<Order> orders = orderService.filterClientOrders(1).get();
        assertTrue(orders.contains(order1));
    }

    @Test
    void deleteOrderByDate() throws ExecutionException, InterruptedException {
       orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
       assertEquals( 3, orderService.getAll().get().size());
       orderService.deleteOrderByDate(LocalDateTime.of(1999, 3, 4, 0, 0), LocalDateTime.of(2019, 3, 4, 0, 0));
       assertEquals(1, orderService.getAll().get().size());
    }

    @Test
    void buyCoffee() throws ExecutionException, InterruptedException {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        assertEquals(3, orderService.getAll().get().size());
        orderService.buyCoffee(4, 3, 2);
        assertEquals(4, orderService.getAll().get().size());
    }

    @Test
    void changeStatus() {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        orderDbRepository.findOne(new ImmutablePair<>(3,3))
                .ifPresentOrElse(ord -> assertEquals(Status.pending, ord.getStatus()), Assertions::fail);
        orderService.changeStatus(3, 3, Status.canceled);
        orderDbRepository.findOne(new ImmutablePair<>(3,3))
                .ifPresentOrElse(ord -> assertEquals(Status.canceled, ord.getStatus()), Assertions::fail);
    }

    @Test
    void deleteCoffeesFromOrder() {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        orderDbRepository.findOne(new ImmutablePair<>(1, 1))
                .ifPresentOrElse(ord -> assertEquals(3, ord.getCoffeesId().size()), Assertions::fail);
        List<Integer> coffees = new ArrayList<>();
        coffees.add(1);
        coffees.add(2);
        orderService.deleteCoffeesFromOrder(1, 1, coffees);
        orderDbRepository.findOne(new ImmutablePair<>(1, 1))
                .ifPresentOrElse(ord -> assertEquals(1, ord.getCoffeesId().size()), Assertions::fail);
    }

    @Test
    void addCoffeesToOrder() {
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);
        orderDbRepository.findOne(new ImmutablePair<>(3, 3))
                .ifPresentOrElse(ord -> assertEquals(1, ord.getCoffeesId().size()), Assertions::fail);
        List<Integer> coffees = new ArrayList<>();
        coffees.add(1);
        coffees.add(2);
        orderService.addCoffeesToOrder(3, 3, coffees);
        orderDbRepository.findOne(new ImmutablePair<>(3, 3))
                .ifPresentOrElse(ord -> assertEquals(3, ord.getCoffeesId().size()), Assertions::fail);
    }
}