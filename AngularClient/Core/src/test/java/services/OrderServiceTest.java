package services;

import config.JpaConfig;
import domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import repositories.CoffeeDbRepository;
import services.interfaces.IClientService;
import services.interfaces.ICoffeeService;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = JpaConfig.class)
class OrderServiceTest {

    private Address address1, address2, address3;
    private Client client1, client2, client3;
    private Coffee coffee1, coffee2, coffee3;
    private ShopOrder order1, order11, order111, order2, order22, order3;

    @Autowired
    private AddressDbRepository addressDbRepository;

    @Autowired
    private ClientDbRepository clientDbRepository;

    @Autowired
    private CoffeeDbRepository coffeeDbRepository;

    @Autowired
    private ICoffeeService coffeeService;

    @Autowired
    private IClientService clientService;

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
        address1 = addressDbRepository.save(address1);
        address2 = addressDbRepository.save(address2);
        address3 = addressDbRepository.save(address3);

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
        address1.getClients().add(client1);
        address2.getClients().add(client2);
        address3.getClients().add(client3);
        address1 = addressDbRepository.save(address1);
        address2 = addressDbRepository.save(address2);
        address3 = addressDbRepository.save(address3);
        client1 = address1.getClients().stream().findFirst().orElse(null);
        client2 = address2.getClients().stream().findFirst().orElse(null);
        client3 = address3.getClients().stream().findFirst().orElse(null);

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
        coffee2 = coffeeDbRepository.save(coffee2);
        coffee3 = coffeeDbRepository.save(coffee3);

        order1 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee1)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        client1.getOrders().add(order1);
        client1 = clientDbRepository.save(client1);

        order11 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee2)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        client1.getOrders().add(order11);
        client1 = clientDbRepository.save(client1);

        order111 = ShopOrder.builder()
                .status(Status.delivered)
                .client(client1)
                .coffee(coffee3)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        client1.getOrders().add(order111);
        client1 = clientDbRepository.save(client1);


        order2 = ShopOrder.builder()
                .client(client2)
                .coffee(coffee1)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        client2.getOrders().add(order2);
        client2 = clientDbRepository.save(client2);

        order22 = ShopOrder.builder()
                .client(client2)
                .coffee(coffee2)
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        client2.getOrders().add(order22);
        client2 = clientDbRepository.save(client2);

        order3 = ShopOrder.builder()
                .client(client3)
                .coffee(coffee3)
                .status(Status.pending)
                .time(LocalDateTime.of(2020, 3, 4, 0, 0))
                .build();
        client3.getOrders().add(order3);
        client3 = clientDbRepository.save(client3);

        List<Coffee> coffees = coffeeDbRepository.findAll();
        coffee1 = coffees.get(0);
        coffee2 = coffees.get(1);
        coffee3 = coffees.get(2);

        List<ShopOrder> orders = clientDbRepository.findAll().stream()
                .flatMap(client -> client.getOrders().stream())
                .collect(Collectors.toList());
        order1 = orders.get(0);
        order2 = orders.get(3);
    }

    @AfterEach
    public void tearDown() {
        clientDbRepository.findAll().forEach(client -> {
            client.getOrders().forEach(ord -> {
                Coffee coffee = ord.getCoffee();
                coffee.getOrders().remove(ord);
                coffeeDbRepository.save(coffee);
                ord.setCoffee(null);
            });
            clientDbRepository.save(client);
        });
        coffeeDbRepository.deleteAll();
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
        orderService.deleteOrderByDate(LocalDateTime.of(1999, 3, 4, 0, 0),
                LocalDateTime.of(2019, 3, 4, 0, 0));
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
        assertEquals((int) orderService.getAll().stream()
                .filter(order -> order.getStatus().equals(Status.canceled)).count(), 1);
    }

    @Test
    public void getAllTest() {
        Set<ShopOrder> shopOrders = orderService.getAll();
        assertEquals(shopOrders.size(), 6);
    }

}