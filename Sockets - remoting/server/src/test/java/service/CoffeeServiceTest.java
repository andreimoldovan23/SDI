package service;

import domain.Coffee;
import domain.Validators.CoffeeValidator;
import domain.Validators.DatabaseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.CoffeeDbRepository;
import repository.Repository;
import services.ICoffeeService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoffeeServiceTest {
    static Coffee c1, c2, c3;
    static Repository<Integer, Coffee> repository;
    static ICoffeeService service;

    @BeforeAll
    public static void setUp() throws Exception {
        c1 = Coffee.Builder()
                .id(10)
                .name("Jacobs")
                .origin("Colombia")
                .price(12)
                .build();
        c2 = Coffee.Builder()
                .id(20)
                .name("Nescafe")
                .origin("Jamaica")
                .price(14)
                .build();
        c3 = Coffee.Builder()
                .id(30)
                .name("HeavyCup")
                .origin("Lebanon")
                .price(15)
                .build();

        repository = new CoffeeDbRepository(new CoffeeValidator(), System.getenv("TESTDB"));
        service = new CoffeeService(repository);

        service.Add(c1);
        service.Add(c2);
        service.Add(c3);
    }

    @AfterAll
    public static void tearDownClass() {
        c1 = null;
        c2 = null;
        c3 = null;
        repository = null;
        service = null;

        Connection conn;
        String db = "jdbc:sqlite:" + System.getenv("TESTDB");
        String dropCoffee = "DROP TABLE COFFEE";
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(db);
            conn.createStatement().executeUpdate("PRAGMA foreign_keys= ON");
            conn.createStatement().execute(dropCoffee);
        } catch (Exception e) {
            throw new DatabaseException("Something went wrong while connecting");
        }
    }

    @Test
    public void filterCoffeesByNameTest() throws ExecutionException, InterruptedException {
        Set<Coffee> nameLikeJ = service.filterCoffeesByName("J").get();
        Set<Coffee> nameLikeA = service.filterCoffeesByName("a").get();


        assertTrue(nameLikeJ.contains(c1));
        assertFalse(nameLikeJ.contains(c3));

        assertTrue(nameLikeA.contains(c3));
        assertTrue(nameLikeA.contains(c2));
        assertTrue(nameLikeA.contains(c1));
    }

    @Test
    public void filterCoffeesByOriginTest() throws ExecutionException, InterruptedException {
        Set<Coffee> coffeesLikeJam = service.filterCoffeesByOrigin("Jam").get();
        Set<Coffee> coffeesLikeCol = service.filterCoffeesByOrigin("Col").get();
        
        assertTrue(coffeesLikeJam.contains(c2));
        assertFalse(coffeesLikeJam.contains(c3));
        
        assertTrue(coffeesLikeCol.contains(c1));
    }

}