package service;

import config.Config;
import domain.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repository.DBRepo;
import services.IOrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
class OrderServiceTest {

    private Coffee coffee1, coffee2, coffee3;
    private Order order1;

    @Autowired
    DBRepo<Integer, Address> addressDbRepository;

    @Autowired
    DBRepo<Integer, Client> clientDbRepository;

    @Autowired
    DBRepo<Integer, Coffee> coffeeDbRepository;

    @Autowired
    DBRepo<Pair<Integer, Integer>, Order> orderDbRepository;

    @Autowired
    private IOrderService orderService;

    @BeforeEach
    public void setUp() {
        Address address1 = Address.Builder()
                .id(1)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Address address2 = Address.Builder()
                .id(2)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Address address3 = Address.Builder()
                .id(3)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        Client client1 = Client.Builder()
                .id(1)
                .firstName("first")
                .lastName("last")
                .addressId(1)
                .build();
        Client client2 = Client.Builder()
                .id(2)
                .firstName("first")
                .lastName("last")
                .addressId(2)
                .build();
        Client client3 = Client.Builder()
                .id(3)
                .firstName("first")
                .lastName("last")
                .addressId(3)
                .build();
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
        order1 = Order.Builder()
                .id(new ImmutablePair<>(1, 1))
                .status(Status.delivered)
                .time(LocalDateTime.of(2000, 3, 4, 0, 0))
                .build();
        order1.getCoffeesId().add(1);
        order1.getCoffeesId().add(2);
        order1.getCoffeesId().add(3);
        Order order2 = Order.Builder()
                .id(new ImmutablePair<>(2, 2))
                .status(Status.delivered)
                .time(LocalDateTime.of(2010, 3, 4, 0, 0))
                .build();
        order2.getCoffeesId().add(2);
        order2.getCoffeesId().add(3);
        Order order3 = Order.Builder()
                .id(new ImmutablePair<>(3, 3))
                .status(Status.pending)
                .time(LocalDateTime.of(2020, 3, 4, 0, 0))
                .build();
        order3.getCoffeesId().add(3);

        addressDbRepository.save(address1);
        addressDbRepository.save(address2);
        addressDbRepository.save(address3);

        clientDbRepository.save(client1);
        clientDbRepository.save(client2);
        clientDbRepository.save(client3);

        coffeeDbRepository.save(coffee1);
        coffeeDbRepository.save(coffee2);
        coffeeDbRepository.save(coffee3);

        orderDbRepository.save(order1);
        orderDbRepository.save(order2);
        orderDbRepository.save(order3);
    }

    @AfterEach
    public void tearDown() {
        coffee1 = null;
        coffee2 = null;
        coffee3 = null;
        order1 = null;
        orderDbRepository.deleteTable();
        coffeeDbRepository.deleteTable();
        clientDbRepository.deleteTable();
        addressDbRepository.deleteTable();
    }

    @Test
    void filterClientCoffees() {
        System.out.println("FilterClientCoffees");
        orderService.getAll().forEach(System.out::println);
        Set<Coffee> coffees = orderService.filterClientCoffees(1);
        assertTrue(coffees.contains(coffee1));
        assertTrue(coffees.contains(coffee2));
        assertTrue(coffees.contains(coffee3));
    }

    @Test
    void filterClientOrders() {
        Set<Order> orders = orderService.filterClientOrders(1);
        assertTrue(orders.contains(order1));
    }

    @Test
    void deleteOrderByDate() {
        System.out.println("DeleteOrderByDate");
        orderService.getAll().forEach(System.out::println);
        assertEquals( 3, orderService.getAll().size());
        orderService.deleteOrderByDate(LocalDateTime.of(1999, 3, 4, 0, 0), LocalDateTime.of(2019, 3, 4, 0, 0));
        assertEquals(1, orderService.getAll().size());
    }

    @Test
    void buyCoffee() {
        assertEquals(3, orderService.getAll().size());
        orderService.buyCoffee(4, 3, 2);
        assertEquals(4, orderService.getAll().size());
    }

    @Test
    void changeStatus() {
        orderDbRepository.findOne(new ImmutablePair<>(3,3))
                .ifPresentOrElse(ord -> assertEquals(Status.pending, ord.getStatus()), Assertions::fail);
        orderService.changeStatus(3, 3, Status.canceled);
        orderDbRepository.findOne(new ImmutablePair<>(3,3))
                .ifPresentOrElse(ord -> assertEquals(Status.canceled, ord.getStatus()), Assertions::fail);
    }

    @Test
    void deleteCoffeesFromOrder() {
        orderDbRepository.findOne(new ImmutablePair<>(1, 1))
                .ifPresentOrElse(ord -> assertEquals(3, ord.getCoffeesId().size()), Assertions::fail);
        List<Integer> coffees = new ArrayList<>();
        coffees.add(1);
        coffees.add(2);
        orderService.deleteCoffeesFromOrder(1, 1, coffees);
        orderDbRepository.findOne(new ImmutablePair<>(1, 1))
                .ifPresentOrElse(ord -> assertEquals(1, ord.getCoffeesId().size()), Assertions::fail);
    }

    @Test
    void addCoffeesToOrder() {
        orderDbRepository.findOne(new ImmutablePair<>(3, 3))
                .ifPresentOrElse(ord -> assertEquals(1, ord.getCoffeesId().size()), Assertions::fail);
        List<Integer> coffees = new ArrayList<>();
        coffees.add(1);
        coffees.add(2);
        orderService.addCoffeesToOrder(3, 3, coffees);
        orderDbRepository.findOne(new ImmutablePair<>(3, 3))
                .ifPresentOrElse(ord -> assertEquals(3, ord.getCoffeesId().size()), Assertions::fail);
    }

}