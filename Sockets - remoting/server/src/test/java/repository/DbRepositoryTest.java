package repository;

import domain.*;
import domain.Validators.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class DbRepositoryTest {

    private static final String dbFilename = System.getenv("TESTDB");
    private Repository<Integer, Address> addressDBRepo;
    private Repository<Integer, Client> clientDBRepo;
    private Repository<Integer, Coffee> coffeeDBRepo;
    private Repository<Pair<Integer, Integer>, Order> orderDBRepo;
    private Client c1, c2, c3;
    private Coffee cof1, cof2, cof3;
    private Address a1, a2;
    private Order o1, o2, o3;

    @BeforeEach
    public void setUp() {
        a1 = Address.Builder()
                .id(10)
                .city("LosAngeles")
                .street("PalmStreet")
                .number(45)
                .build();
        a2 = Address.Builder()
                .id(20)
                .city("NewYoark")
                .street("WallStreet")
                .number(65)
                .build();

        c1 = Client.Builder()
                .id(10)
                .firstName("John")
                .lastName("Doe")
                .addressId(10)
                .build();
        c2 = Client.Builder()
                .id(20)
                .firstName("Clark")
                .lastName("Kent")
                .addressId(20)
                .age(35)
                .build();
        c3 = Client.Builder()
                .id(30)
                .firstName("Wally")
                .lastName("West")
                .addressId(10)
                .age(21)
                .phoneNumber("0748933465")
                .build();

        cof1 = Coffee.Builder()
                .id(10)
                .name("BlueMountain")
                .origin("Jamaica")
                .build();
        cof2 = Coffee.Builder()
                .id(20)
                .name("Yirgacheffe")
                .origin("Ethiopia")
                .quantity(200)
                .build();
        cof3 = Coffee.Builder()
                .id(30)
                .name("Arabica")
                .origin("Brazil")
                .quantity(300)
                .price(50)
                .build();

        o1 = Order.Builder()
                .id(new ImmutablePair<>(10, 10))
                .status(Status.outOfStock)
                .time(LocalDateTime.now())
                .build();
        o2 = Order.Builder()
                .id(new ImmutablePair<>(20, 20))
                .build();
        o3 = Order.Builder()
                .id(new ImmutablePair<>(30, 30))
                .status(Status.delivered)
                .build();
        o1.getCoffeesId().add(10);
        o2.getCoffeesId().add(10);
        o2.getCoffeesId().add(20);
        o3.getCoffeesId().add(30);
        o3.getCoffeesId().add(20);

        addressDBRepo = new AddressDbRepository(new AddressValidator(), dbFilename);
        clientDBRepo = new ClientDbRepository(new ClientValidator(), dbFilename);
        coffeeDBRepo = new CoffeeDbRepository(new CoffeeValidator(), dbFilename);
        orderDBRepo = new OrderDbRepository(new OrderValidator(), dbFilename);
    }

    @AfterEach
    public void tearDown() {
        a1 = null;
        a2 = null;
        c1 = null;
        c2 = null;
        c3 = null;
        cof1 = null;
        cof2 = null;
        cof3 = null;
        o1 = null;
        o2 = null;
        o3 = null;
        addressDBRepo = null;
        clientDBRepo = null;
        coffeeDBRepo = null;
        orderDBRepo = null;
    }

    @AfterAll
    public static void tearDownClass() {
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
    @org.junit.jupiter.api.Order(1)
    public void operationsTest() {
        //add
        addressDBRepo.save(a1);
        addressDBRepo.save(a2);

        clientDBRepo.save(c1);
        clientDBRepo.save(c2);
        clientDBRepo.save(c3);

        Client c4 = Client.Builder()
                .id(50)
                .firstName("Jack")
                .lastName("Daniel")
                .addressId(100)
                .build();
        assertThrows(DatabaseException.class, () -> clientDBRepo.save(c4));
        assertThrows(DatabaseException.class, () -> clientDBRepo.save(c1));

        coffeeDBRepo.save(cof1);
        coffeeDBRepo.save(cof2);
        coffeeDBRepo.save(cof3);

        orderDBRepo.save(o1);
        orderDBRepo.save(o2);
        orderDBRepo.save(o3);

        Order orderWrong1 = Order.Builder()
                .id(new ImmutablePair<>(100, 400))
                .build();
        Order orderWrong2 = Order.Builder()
                .id(new ImmutablePair<>(100, 10))
                .build();
        orderWrong1.getCoffeesId().add(10);
        orderWrong2.getCoffeesId().add(200);
        assertThrows(DatabaseException.class, () -> orderDBRepo.save(orderWrong1));
        assertThrows(DatabaseException.class, () -> orderDBRepo.save(orderWrong2));

        //update
        c2.setAge(44);
        cof2.setPrice(700);
        o2.setStatus(Status.pending);
        o2.getCoffeesId().remove((Object)10);
        o2.getCoffeesId().add(30);
        a1.setNumber(70);

        addressDBRepo.update(a1);
        clientDBRepo.update(c1);
        coffeeDBRepo.update(cof2);
        orderDBRepo.update(o2);

        c1.setAddressId(500);
        o2.setId(new ImmutablePair<>(20, 400));
        o3.getCoffeesId().add(500);

        assertThrows(DatabaseException.class, () -> clientDBRepo.update(c1));
        assertThrows(DatabaseException.class, () -> orderDBRepo.update(o2));
        assertThrows(DatabaseException.class, () -> orderDBRepo.update(o3));

        //delete
        orderDBRepo.delete(new ImmutablePair<>(10, 10));
        coffeeDBRepo.delete(10);
        clientDBRepo.delete(10);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void loadDataTest() {
        addressDBRepo = new AddressDbRepository(new AddressValidator(), dbFilename);
        clientDBRepo = new ClientDbRepository(new ClientValidator(), dbFilename);
        coffeeDBRepo = new CoffeeDbRepository(new CoffeeValidator(), dbFilename);
        orderDBRepo = new OrderDbRepository(new OrderValidator(), dbFilename);

        Set<Address> addresses = StreamSupport.stream(addressDBRepo.findAll().spliterator(), false)
                .collect(Collectors.toSet());
        Set<Client> clients = StreamSupport.stream(clientDBRepo.findAll().spliterator(), false)
                .collect(Collectors.toSet());
        Set<Coffee> coffees = StreamSupport.stream(coffeeDBRepo.findAll().spliterator(), false)
                .collect(Collectors.toSet());
        Set<Order> orders = StreamSupport.stream(orderDBRepo.findAll().spliterator(), false)
                .collect(Collectors.toSet());

        c2.setAge(44);
        cof2.setPrice(700);
        o2.setStatus(Status.pending);
        o2.getCoffeesId().remove((Object)10);
        o2.getCoffeesId().add(30);
        a1.setNumber(70);

        assertEquals(2, addresses.size());
        assertEquals(2, clients.size());
        assertEquals(2, coffees.size());
        assertEquals(2, orders.size());

        assertTrue(addresses.contains(a1));
        assertTrue(addresses.contains(a2));

        assertTrue(clients.contains(c2));
        assertTrue(clients.contains(c3));

        assertTrue(coffees.contains(cof2));
        assertTrue(coffees.contains(cof3));

        assertTrue(orders.contains(o2));
        assertTrue(orders.contains(o3));
    }

}