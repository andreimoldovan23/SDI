package services;

import config.JpaConfig;
import domain.*;
import domain.Validators.ValidatorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import repositories.CoffeeDbRepository;
import repositories.OrderDbRepository;
import services.interfaces.IClientService;
import services.interfaces.ICoffeeService;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = JpaConfig.class)
class OrderServiceTest {

    private Address address1, address2, address3;
    private Client client1, client2, client3;
    private Coffee coffee1, coffee2, coffee3;
    private ShopOrder order1, order11, order111, order2, order22, order3;

    @Autowired
    AddressDbRepository addressDbRepository;

    @Autowired
    ClientDbRepository clientDbRepository;

    @Autowired
    CoffeeDbRepository coffeeDbRepository;

    @Autowired
    OrderDbRepository orderDbRepository;

    @Autowired
    ICoffeeService coffeeService;

    @Autowired
    IClientService clientService;

    @Autowired
    private IOrderService orderService;

    @BeforeEach
    public void setUp() {
        address1 = Address.builder()
                .city("Los Angeles")
                .street("PalmStreet")
                .number(1)
                .build();
        address2 = Address.builder()
                .city("Moscow")
                .street("Kremlin")
                .number(1)
                .build();
        address3 = Address.builder()
                .city("Los Angeles")
                .street("PalmStreet")
                .number(12)
                .build();
        addressDbRepository.save(address1);
        addressDbRepository.save(address2);
        addressDbRepository.save(address3);

        client1 = Client.builder()
                .firstName("first")
                .lastName("last")
                .address(address1)
                .build();
        client2 = Client.builder()
                .firstName("first")
                .lastName("last")
                .address(address2)
                .build();
        client3 = Client.builder()
                .firstName("first")
                .lastName("last")
                .address(address3)
                .build();
        client1 = clientDbRepository.save(client1);
        clientDbRepository.save(client2);
        clientDbRepository.save(client3);

        coffee1 = Coffee.builder()
                .name("Jacobs")
                .origin("Colombia")
                .price(12)
                .build();
        coffee2 = Coffee.builder()
                .name("Nescafe")
                .origin("Jamaica")
                .price(14)
                .build();
        coffee3 = Coffee.builder()
                .name("HeavyCup")
                .origin("Lebanon")
                .price(15)
                .build();
        coffee1 = coffeeDbRepository.save(coffee1);
        coffeeDbRepository.save(coffee2);
        coffeeDbRepository.save(coffee3);

        order1 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee1)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order11 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee2)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order111 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee3)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();


        order2 = ShopOrder.builder()
                .client(client2)
                .coffee(coffee1)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        order22 = ShopOrder.builder()
                .client(client2)
                .coffee(coffee2)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();

        order3 = ShopOrder.builder()
                .client(client3)
                .coffee(coffee3)
                .status(Status.pending)
                .time(LocalDateTime.of(2020, 3, 4, 0, 0))
                .build();

        order1 = orderDbRepository.save(order1);
        order11 = orderDbRepository.save(order11);
        order111 = orderDbRepository.save(order111);

        order2 = orderDbRepository.save(order2);
        order22 = orderDbRepository.save(order22);

        order3 = orderDbRepository.save(order3);

    }

    @AfterEach
    public void tearDown() {
        address1 = null;
        address2 = null;
        address3 = null;
        client1 = null;
        client2 = null;
        client3 = null;
        coffee1 = null;
        coffee2 = null;
        coffee3 = null;
        order1 = null;
        order11 = null;
        order111 = null;
        order2 = null;
        order22 = null;
        order3 = null;

        orderDbRepository.deleteAll();
        coffeeDbRepository.deleteAll();
        clientDbRepository.deleteAll();
        addressDbRepository.deleteAll();
    }

    @Test
    public void howManyCoffeesClientOrdered() {
        assertEquals(clientService.howManyCoffeesClientOrdered(client1.getId()), 3);
    }

    @Test
    public void byHowManyClientsCoffeeOrdered() {
        assertEquals(coffeeService.byHowManyClientsWasOrdered(coffee1.getId()), 2);
    }

    @Test
    void filterClientCoffees() {
        Set<Coffee> coffees = orderService.filterClientCoffees(client1.getId());
        assertTrue(coffees.contains(coffee1));
        assertTrue(coffees.contains(coffee2));
        assertTrue(coffees.contains(coffee3));
    }

    @Test
    void filterClientOrders() {
        Set<ShopOrder> orders = orderService.filterClientOrders(client1.getId());
        assertTrue(orders.contains(order1));
    }

    @Test
    void deleteOrderByDate() {
        assertEquals( 6, orderService.getAll().size());
        orderService.deleteOrderByDate(LocalDateTime.of(1999, 3, 4, 0, 0), LocalDateTime.of(2019, 3, 4, 0, 0));
        assertEquals(1, orderService.getAll().size());
    }

    @Test
    void buyCoffee() {
        assertEquals(6, orderService.getAll().size());
        orderService.buyCoffee(client3.getId(), coffee2.getId());
        assertEquals(7, orderService.getAll().size());
    }

    @Test
    void changeStatus() {
        orderService.changeStatus(order2.getId(), Status.canceled);
        assertTrue(orderDbRepository.findById(order2.getId()).isPresent());
        assertEquals(Status.canceled, orderDbRepository.findById(order2.getId()).get().getStatus());
    }

    @Test
    public void getAllTest() {
        Set<ShopOrder> shopOrders = orderService.getAll();
        assertEquals(shopOrders.size(), 6);
    }

    @Test
    public void getOneTest() {
        ShopOrder shopOrder = orderService.findOne(order1.getId());
        assertEquals(shopOrder, order1);
    }

    @Test
    public void getOneBadTest() {
        assertThrows(ValidatorException.class, () -> orderService.findOne(200));
    }

    @Test
    public void deleteTest() {
        assertThrows(DataAccessException.class, () -> orderService.Delete(200));
        assertEquals(orderService.getAll().size(), 6);

        orderService.Delete(order1.getId());
        assertEquals(orderService.getAll().size(), 5);
    }

    @Test
    public void updateTest() {
        order1.setStatus(Status.canceled);

        orderService.Update(order1);
        assertEquals(orderService.findOne(order1.getId()).getStatus(), Status.canceled);

        order1.setId(200);
        assertThrows(ValidatorException.class, () -> orderService.Update(order1));
    }

}