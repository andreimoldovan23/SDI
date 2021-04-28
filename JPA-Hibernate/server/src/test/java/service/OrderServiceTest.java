package service;

import serverConfig.Config;
import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repository.AddressDbRepository;
import repository.ClientDbRepository;
import repository.CoffeeDbRepository;
import repository.OrderDbRepository;
import services.IOrderService;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
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
    private IOrderService orderService;

    @BeforeEach
    public void setUp() {
        address1 = Address.Builder()
                .id(1)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        address2 = Address.Builder()
                .id(2)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        address3 = Address.Builder()
                .id(3)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        addressDbRepository.save(address1);
        addressDbRepository.save(address2);
        addressDbRepository.save(address3);

        client1 = Client.Builder()
                .id(1)
                .firstName("first")
                .lastName("last")
                .address(address1)
                .build();
        client2 = Client.Builder()
                .id(2)
                .firstName("first")
                .lastName("last")
                .address(address2)
                .build();
        client3 = Client.Builder()
                .id(3)
                .firstName("first")
                .lastName("last")
                .address(address3)
                .build();
        clientDbRepository.save(client1);
        clientDbRepository.save(client2);
        clientDbRepository.save(client3);

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
        coffeeDbRepository.save(coffee1);
        coffeeDbRepository.save(coffee2);
        coffeeDbRepository.save(coffee3);

        order1 = ShopOrder.Builder()
                .id(1)
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee1)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order11 = ShopOrder.Builder()
                .id(11)
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee2)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order111 = ShopOrder.Builder()
                .id(111)
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee3)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();


        order2 = ShopOrder.Builder()
                .id(2)
                .client(client2)
                .coffee(coffee1)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        order22 = ShopOrder.Builder()
                .id(22)
                .client(client2)
                .coffee(coffee2)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();

        order3 = ShopOrder.Builder()
                .id(3)
                .client(client3)
                .coffee(coffee3)
                .status(Status.pending)
                .time(LocalDateTime.of(2020, 3, 4, 0, 0))
                .build();

        orderDbRepository.save(order1);
        orderDbRepository.save(order11);
        orderDbRepository.save(order111);

        orderDbRepository.save(order2);
        orderDbRepository.save(order22);

        orderDbRepository.save(order3);

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
        orderService.buyCoffee(4, client3.getId(), coffee2.getId());
        assertEquals(7, orderService.getAll().size());
    }

    @Test
    void changeStatus() {
        orderService.changeStatus(3, Status.canceled);
        assertTrue(orderDbRepository.findById(3).isPresent());
        assertEquals(Status.canceled, orderDbRepository.findById(3).get().getStatus());
    }

}