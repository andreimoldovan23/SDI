package service;

import domain.Address;
import domain.Validators.AddressValidator;
import domain.Validators.DatabaseException;
import org.junit.jupiter.api.*;
import repository.AddressDbRepository;
import repository.Repository;
import services.IAddressService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AddressServiceTest {

    static Address a1, a2, a3;
    static Repository<Integer, Address> repository;
    static IAddressService service;

    @BeforeAll
    public static void setUpClass() throws Exception {
        a1 = Address.Builder()
                .id(14)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        a2 = Address.Builder()
                .id(15)
                .city("cccc")
                .street("dddd")
                .number(2)
                .build();
        a3 = Address.Builder()
                .id(16)
                .city("eeee")
                .street("ffff")
                .number(1)
                .build();

        repository = new AddressDbRepository(new AddressValidator(), System.getenv("TESTDB"));
        service = new AddressService(repository);

        service.Add(a1);
        service.Add(a2);
        service.Add(a3);
    }

    @AfterAll
    public static void tearDownClass() {
        Connection conn;
        String db = "jdbc:sqlite:" + System.getenv("TESTDB");
        String dropAddress = "DROP TABLE ADDRESS";

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(db);
            conn.createStatement().executeUpdate("PRAGMA foreign_keys= ON");
            conn.createStatement().execute(dropAddress);
        } catch (Exception e) {
            throw new DatabaseException("Something went wrong while connecting");
        }

        a1 = null;
        a2 = null;
        a3 = null;
        repository = null;
        service = null;
    }


    @Test
    public void deleteAddressByNumber() throws ExecutionException, InterruptedException {
        service.deleteAddressWithNumber(1);
        Set<Address> addresses = service.getAll().get();
        assertFalse(addresses.contains(a1));
    }

}
