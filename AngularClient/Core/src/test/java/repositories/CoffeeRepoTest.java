package repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Coffee;
import domain.Validators.CoffeeValidatorException;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class CoffeeRepoTest {

    @Autowired
    private CoffeeDbRepository coffeeDbRepository;

    @Test
    public void testFindAllWithOrders() {
        Set<Coffee> coffees = coffeeDbRepository.findAllWithOrders();

        assertEquals(coffees.size(), 3);
        coffees.forEach(coffee -> {
            assertTrue(coffee.getOrders().size() > 0);
            coffee.getOrders().forEach(shopOrder -> {
                assertNotNull(shopOrder.getClient());
                assertNotNull(shopOrder.getCoffee());
            });
        });
    }

    @Test
    public void testFindByIdWithOrders() {
        Coffee coffee = coffeeDbRepository.findByIdWithOrders(1).orElseThrow(() -> new CoffeeValidatorException("Invalid coffee id"));

        assertEquals(coffee.getOrders().size(), 5);
        coffee.getOrders().forEach(shopOrder -> {
            assertNotNull(shopOrder.getClient());
            assertEquals(shopOrder.getCoffee(), coffee);
        });
    }

    @Test(expected = LazyInitializationException.class)
    public void testFilterByName() {
        Set<Coffee> coffees = coffeeDbRepository.filterCoffeesByName("Blue Mountain");

        assertEquals(coffees.size(), 1);

        List<String> coffeeNames = coffees.stream()
                .map(Coffee::getName)
                .collect(Collectors.toList());

        assertTrue(coffeeNames.contains("Blue Mountain"));

        coffees.forEach(coffee -> System.out.println(coffee.getOrders()));
    }

    @Test(expected = LazyInitializationException.class)
    public void testFilterByOrigin() {
        Set<Coffee> coffees = coffeeDbRepository.filterCoffeesByOrigin("Jamaica");

        assertEquals(coffees.size(), 1);

        List<String> coffeeNames = coffees.stream()
                .map(Coffee::getName)
                .collect(Collectors.toList());

        assertTrue(coffeeNames.contains("Blue Mountain"));

        coffees.forEach(coffee -> System.out.println(coffee.getOrders()));
    }

}
