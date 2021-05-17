package services;

import config.JpaConfig;
import domain.Coffee;
import domain.Validators.ValidatorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repositories.CoffeeDbRepository;
import services.interfaces.ICoffeeService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = JpaConfig.class)
public class CoffeeServiceTest {

    private Coffee c1, c2, c3;

    @Autowired
    private CoffeeDbRepository coffeeDBRepo;

    @Autowired
    private ICoffeeService service;

    @BeforeEach
    public void setUp() {
        c1 = Coffee.builder()
                .name("Jacobs")
                .origin("Colombia")
                .price(12)
                .build();
        c2 = Coffee.builder()
                .name("Nescafe")
                .origin("Jamaica")
                .price(14)
                .build();
        c3 = Coffee.builder()
                .name("HeavyCup")
                .origin("Lebanon")
                .price(15)
                .build();

        service.Add(c1);
        service.Add(c2);
        service.Add(c3);

        List<Coffee> coffees = coffeeDBRepo.findAll();
        c1 = coffees.get(0);
        c2 = coffees.get(1);
        c3 = coffees.get(2);
    }

    @AfterEach
    public void tearDown() {
        c1 = null;
        c2 = null;
        c3 = null;
        coffeeDBRepo.deleteAll();
    }

    @Test
    public void filterCoffeesByNameTest() {
        Set<Coffee> nameLikeJ = service.filterCoffeesByName("J");
        Set<Coffee> nameLikeA = service.filterCoffeesByName("a");

        assertTrue(nameLikeJ.contains(c1));
        assertFalse(nameLikeJ.contains(c3));

        assertTrue(nameLikeA.contains(c3));
        assertTrue(nameLikeA.contains(c2));
        assertTrue(nameLikeA.contains(c1));
    }

    @Test
    public void filterCoffeesByOriginTest() {
        Set<Coffee> coffeesLikeJam = service.filterCoffeesByOrigin("Jam");
        Set<Coffee> coffeesLikeCol = service.filterCoffeesByOrigin("Col");

        assertTrue(coffeesLikeJam.contains(c2));
        assertFalse(coffeesLikeJam.contains(c3));

        assertTrue(coffeesLikeCol.contains(c1));
    }

    @Test
    public void getAllTest() {
        Set<Coffee> coffees = service.getAll();
        assertEquals(coffees.size(), 3);
    }

    @Test
    public void getOneTest() {
        Coffee coffee = service.findOne(c1.getId());
        assertEquals(coffee, c1);
    }

    @Test
    public void getOneBadTest() {
        assertThrows(ValidatorException.class, () -> service.findOne(1000));
    }

    @Test
    public void deleteTest() {
        service.Delete(c1.getId());
        assertEquals(service.getAll().size(), 2);
    }

    @Test
    public void deleteBadIdTest() {
        assertThrows(ValidatorException.class, () -> service.Delete(1000));
        assertEquals(service.getAll().size(), 3);
    }

    @Test
    public void updateTest() {
        c1.setPrice(45);
        service.Update(c1);
        assertEquals(service.findOne(c1.getId()).getPrice(), 45);
    }

    @Test
    public void updateBadIdTest() {
        c1.setId(1000);
        assertThrows(ValidatorException.class, () -> service.Update(c1));
    }

    @Test
    public void updateNotValidTest() {
        c1.setName(null);
        assertThrows(ValidatorException.class, () -> service.Update(c1));

        c1.setName("Los- Mandingas");
        assertThrows(ValidatorException.class, () -> service.Update(c1));

        c1.setName("Los Mandingas");
        c1.setQuantity(-3);
        assertThrows(ValidatorException.class, () -> service.Update(c1));
    }

    @Test
    public void addAlreadyExistentTest() {
        assertThrows(ValidatorException.class, () -> service.Add(c1));
    }

    @Test
    public void addNotValidTest() {
        c1.setId(null);

        c1.setName(null);
        assertThrows(ValidatorException.class, () -> service.Add(c1));

        c1.setName("Los- Mandingas");
        assertThrows(ValidatorException.class, () -> service.Add(c1));

        c1.setName("Los Mandingas");
        c1.setQuantity(-3);
        assertThrows(ValidatorException.class, () -> service.Add(c1));
    }

}