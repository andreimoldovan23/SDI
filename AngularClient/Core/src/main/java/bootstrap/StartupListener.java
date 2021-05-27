package bootstrap;

import domain.*;
import domain.Validators.ClientValidatorException;
import domain.Validators.CoffeeValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositories.addressFragments.AddressDbRepository;
import repositories.clientFragments.ClientDbRepository;
import repositories.coffeeFragments.CoffeeDbRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("DuplicatedCode")
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private AddressDbRepository addressDbRepository;

    @Autowired
    private ClientDbRepository clientDbRepository;

    @Autowired
    private CoffeeDbRepository coffeeDbRepository;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Address address1 = Address.builder()
            .city("Los Angeles")
            .street("Palm Street")
            .number(45)
            .build();

        Address address2 = Address.builder()
                .city("Moscow")
                .street("Kremlin")
                .number(120)
                .build();

        Address address3 = Address.builder()
                .city("Las Vegas")
                .street("Casino Street")
                .number(120)
                .build();

        Address address4 = Address.builder()
                .city("Los Angeles")
                .street("Beverly Hills")
                .number(120)
                .build();

        Address address5 = Address.builder()
                .city("Cluj")
                .street("Dorobantilor")
                .number(45)
                .build();
        //#######################################

        Client client1 = Client.builder()
                .firstName("Mike").lastName("Tyson")
                .build();
        client1.setAge(45);
        client1.setPhoneNumber("+40723747949");
        client1.setAddress(address1);
        address1.getClients().add(client1);

        Client client2 = Client.builder()
                .firstName("Mike").lastName("Johnson").build();
        client2.setAge(50);
        client2.setPhoneNumber("+40723747949");
        client2.setAddress(address1);
        address1.getClients().add(client2);

        Client client3 = Client.builder()
                .firstName("John").lastName("Johnson").build();
        client3.setAge(40);
        client3.setPhoneNumber("+40723747949");
        client3.setAddress(address2);
        address2.getClients().add(client3);

        Client client4 = Client.builder()
                .firstName("Johnson").lastName("Gregory").build();
        client4.setAge(35);
        client4.setPhoneNumber("+40723747949");
        client4.setAddress(address3);
        address3.getClients().add(client4);

        Client client5 = Client.builder()
                .firstName("Ezekiel").lastName("Bull").build();
        client5.setAge(30);
        client5.setPhoneNumber("+40723747949");
        client5.setAddress(address4);
        address4.getClients().add(client5);
        //#######################################

        addressDbRepository.save(address1);
        addressDbRepository.save(address2);
        addressDbRepository.save(address3);
        addressDbRepository.save(address4);
        addressDbRepository.save(address5);

        client1 = findClient(1);
        client2 = findClient(2);
        client3 = findClient(3);
        client4 = findClient(4);
        client5 = findClient(5);
        //#######################################

        Coffee coffee1 = Coffee.builder()
                .name("Blue Mountain").origin("Jamaica")
                .quantity(100).price(100)
                .build();

        Coffee coffee2 = Coffee.builder()
                .name("Yirgacheffe").origin("Ethiopia")
                .quantity(100).price(100)
                .build();

        Coffee coffee3 = Coffee.builder()
                .name("Hamaran").origin("Saudi Arabia")
                .quantity(100).price(100)
                .build();

        coffeeDbRepository.save(coffee1);
        coffeeDbRepository.save(coffee2);
        coffeeDbRepository.save(coffee3);
        coffee1 = findCoffee(1);
        coffee2 = findCoffee(2);
        coffee3 = findCoffee(3);
        //#######################################

        ShopOrder order1 = ShopOrder.builder()
                .client(client1).coffee(coffee1)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client1.getOrders().add(order1);
        coffee1.getOrders().add(order1);

        ShopOrder order2 = ShopOrder.builder()
                .client(client1).coffee(coffee2)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client1.getOrders().add(order2);
        coffee2.getOrders().add(order2);

        ShopOrder order3 = ShopOrder.builder()
                .client(client1).coffee(coffee3)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client1.getOrders().add(order3);
        coffee3.getOrders().add(order3);

        ShopOrder order4 = ShopOrder.builder()
                .client(client3).coffee(coffee1)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client3.getOrders().add(order4);
        coffee1.getOrders().add(order4);

        ShopOrder order5 = ShopOrder.builder()
                .client(client4).coffee(coffee3)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client4.getOrders().add(order5);
        coffee3.getOrders().add(order5);

        ShopOrder order6 = ShopOrder.builder()
                .client(client5).coffee(coffee2)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client5.getOrders().add(order6);
        coffee2.getOrders().add(order6);

        ShopOrder order7 = ShopOrder.builder()
                .client(client5).coffee(coffee1)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client5.getOrders().add(order7);
        coffee1.getOrders().add(order7);

        ShopOrder order8 = ShopOrder.builder()
                .client(client2).coffee(coffee1)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client2.getOrders().add(order8);
        coffee1.getOrders().add(order8);

        ShopOrder order9 = ShopOrder.builder()
                .client(client2).coffee(coffee3)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client2.getOrders().add(order9);
        coffee3.getOrders().add(order9);

        ShopOrder order10 = ShopOrder.builder()
                .client(client1).coffee(coffee1)
                .status(Status.pending).time(LocalDateTime.now())
                .build();
        client1.getOrders().add(order10);
        coffee1.getOrders().add(order10);

        clientDbRepository.saveAll(new ArrayList<>(List.of(client1, client2, client3, client4, client5)));
        coffeeDbRepository.saveAll(new ArrayList<>(List.of(coffee1, coffee2, coffee3)));
    }

    private Client findClient(Integer id) {
        return clientDbRepository.findByIdWithOrders(id).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
    }

    private Coffee findCoffee(Integer id) {
        return coffeeDbRepository.findByIdWithOrders(id).orElseThrow(() -> new CoffeeValidatorException("Invalid coffee id"));
    }

}
