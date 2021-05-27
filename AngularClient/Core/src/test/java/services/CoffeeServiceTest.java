package services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Coffee;
import domain.Validators.CoffeeValidatorException;
import domain.Validators.ValidatorException;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import repositories.coffeeFragments.CoffeeDbRepository;
import services.interfaces.ICoffeeService;

import java.util.Set;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class CoffeeServiceTest {

    @Autowired
    private CoffeeDbRepository coffeeDBRepo;

    @Autowired
    private ICoffeeService service;

    private Coffee findOne(Integer id) {
        return coffeeDBRepo.findByIdWithOrders(id).orElseThrow(() -> new CoffeeValidatorException("Invalid coffee id"));
    }

    @Test(expected = LazyInitializationException.class)
    public void filterCoffeesByNameTest() {
        Set<Coffee> nameLikeMountain = service.filterCoffeesByName("Blue Mountain");

        assertEquals(nameLikeMountain.size(), 1);
        nameLikeMountain.forEach(coffee -> System.out.println(coffee.getOrders()));
    }

    @Test(expected = LazyInitializationException.class)
    public void filterCoffeesByOriginTest() {
        Set<Coffee> coffeesLikeEthiopia = service.filterCoffeesByOrigin("Ethiopia");

        assertEquals(coffeesLikeEthiopia.size(), 1);
        coffeesLikeEthiopia.forEach(coffee -> System.out.println(coffee.getOrders()));
    }

    @Test
    public void getAllTest() {
        Set<Coffee> coffees = service.getAll();
        assertEquals(coffees.size(), 3);
    }

    @Test
    public void deleteTest() {
        service.Delete(1);
        assertEquals(service.getAll().size(), 2);
    }

    @Test(expected = ValidatorException.class)
    public void deleteBadIdTest() {
        service.Delete(1000);
    }

    @Test
    public void updateTest() {
        Coffee c1 = findOne(1);
        c1.setPrice(45);
        service.Update(c1);
        assertEquals(findOne(1).getPrice(), Integer.valueOf(45));
    }

    @Test(expected = ValidatorException.class)
    public void updateBadIdTest() {
        Coffee c1 = findOne(1);
        c1.setId(1000);
        service.Update(c1);
    }

    @Test(expected = ValidatorException.class)
    public void updateNotValidTest() {
        Coffee c1 = findOne(1);

        c1.setName(null);
        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setName("Los- Mandingas");
        }

        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setName("Los Mandingas");
            c1.setQuantity(-3);
        }

        service.Update(c1);
    }

    @Test(expected = ValidatorException.class)
    public void addAlreadyExistentTest() {
        Coffee c1 = findOne(1);
        service.Add(c1);
    }

    @Test(expected = ValidatorException.class)
    public void addNotValidTest() {
        Coffee c1 = findOne(1);
        c1.setId(null);

        c1.setName(null);
        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setName("Los- Mandingas");
        }

        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setName("Los Mandingas");
            c1.setQuantity(-3);
        }

        service.Add(c1);
    }

    @Test
    public void addTest() {
        Coffee coffee = Coffee.builder().name("Arabica").origin("Brazil").quantity(100).price(100).build();
        service.Add(coffee);
        coffee.setId(4);
        assertEquals(findOne(4), coffee);
    }

}