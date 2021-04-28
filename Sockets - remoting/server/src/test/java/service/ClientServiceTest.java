package service;

import domain.Address;
import domain.Client;
import domain.Validators.AddressValidator;
import domain.Validators.ClientValidator;
import domain.Validators.DatabaseException;
import org.junit.jupiter.api.*;
import repository.AddressDbRepository;
import repository.ClientDbRepository;
import repository.Repository;
import services.IClientService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientServiceTest {

    static Client c1, c2, c3;
    static IClientService service;

    @BeforeAll
    public static void setUpClass() {
        Address address = Address.Builder()
                .id(10)
                .city("LasVegas")
                .street("Roulette")
                .number(45)
                .build();
        Repository<Integer, Address> addressRepository = new AddressDbRepository(new AddressValidator(),
                System.getenv("TESTDB"));
        addressRepository.save(address);

        c1 = Client.Builder()
                .id(10)
                .firstName("John")
                .lastName("Jackson")
                .addressId(10)
                .age(40)
                .build();
        c2 = Client.Builder()
                .id(20)
                .firstName("Abdul")
                .lastName("Johnson")
                .addressId(10)
                .age(20)
                .build();
        c3 = Client.Builder()
                .id(30)
                .firstName("Mike")
                .lastName("Jack")
                .addressId(10)
                .build();

        Repository<Integer, Client> repository = new ClientDbRepository(new ClientValidator(),
                System.getenv("TESTDB"));
        repository.save(c1);
        repository.save(c2);
        repository.save(c3);
        service = new ClientService(repository);
    }

    @AfterAll
    public static void tearDownClass() {
        Connection conn;
        String db = "jdbc:sqlite:" + System.getenv("TESTDB");
        String dropAddress = "DROP TABLE ADDRESS";
        String dropClient = "DROP TABLE CLIENT";

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(db);
            conn.createStatement().executeUpdate("PRAGMA foreign_keys= ON");
            conn.createStatement().execute(dropClient);
            conn.createStatement().execute(dropAddress);
        } catch (Exception e) {
            throw new DatabaseException("Something went wrong while connecting");
        }

        c1 = null;
        c2 = null;
        c3 = null;
        service = null;
    }

    @Test
    public void filterClientsByNameTest() throws ExecutionException, InterruptedException {
        Set<Client> nameLikeJack = service.filterClientsByName("Jack").get();
        Set<Client> nameLikeJohn = service.filterClientsByName("John").get();

        assertTrue(nameLikeJack.contains(c1));
        assertTrue(nameLikeJack.contains(c3));

        assertTrue(nameLikeJohn.contains(c2));
        assertTrue(nameLikeJohn.contains(c1));
    }

    @Test
    public void filterClientsInAgeIntervalTest() throws ExecutionException, InterruptedException {
        Set<Client> clients = service.filterClientsInAgeInterval(30, 50).get();
        assertTrue(clients.contains(c1));
    }

}