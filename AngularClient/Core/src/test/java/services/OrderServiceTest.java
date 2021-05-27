package services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Coffee;
import domain.ShopOrder;
import domain.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import services.interfaces.IClientService;
import services.interfaces.ICoffeeService;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class OrderServiceTest {

    @Autowired
    private ICoffeeService coffeeService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private IOrderService orderService;

    @Test
    public void howManyCoffeesClientOrdered() {
        assertEquals(clientService.howManyCoffeesClientOrdered(1), Integer.valueOf(3));
    }

    @Test
    public void byHowManyClientsCoffeeOrdered() {
        assertEquals(coffeeService.byHowManyClientsWasOrdered(1), Integer.valueOf(4));
    }

    @Test
    public void filterClientCoffees() {
        Set<Coffee> coffees = orderService.filterClientCoffees(1);
        Set<String> coffeeNames = coffees.stream().map(Coffee::getName).collect(Collectors.toSet());
        assertEquals(coffees.size(), 3);
        assertTrue(coffeeNames.contains("Blue Mountain"));
        assertTrue(coffeeNames.contains("Yirgacheffe"));
        assertTrue(coffeeNames.contains("Hamaran"));
    }

    @Test
    public void filterClientOrders() {
        Set<ShopOrder> orders = orderService.filterClientOrders(1);
        assertEquals(orders.size(), 4);
    }

    @Test
    public void deleteOrderByDate() {
        assertEquals( 10, orderService.getAll().size());
        orderService.deleteOrderByDate(LocalDateTime.of(1999, 3, 4, 0, 0),
                LocalDateTime.of(2011, 3, 4, 0, 0));
        assertEquals(6, orderService.getAll().size());
    }

    @Test
    public void buyCoffee() {
        assertEquals(10, orderService.getAll().size());
        orderService.buyCoffee(5, 3);
        assertEquals(11, orderService.getAll().size());
    }

    @Test
    public void changeStatus() {
        orderService.changeStatus(1, Status.canceled);
        assertEquals((int) orderService.getAll().stream()
                .filter(order -> order.getStatus().equals(Status.canceled)).count(), 1);
    }

    @Test
    public void getAllTest() {
        Set<ShopOrder> shopOrders = orderService.getAll();
        assertEquals(shopOrders.size(), 10);
    }

}