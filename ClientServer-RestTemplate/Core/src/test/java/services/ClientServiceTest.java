package services;

import config.JpaConfig;
import domain.Address;
import domain.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import services.interfaces.IClientService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = JpaConfig.class)
public class ClientServiceTest {

    private Client c1, c2, c3;

    @Autowired
    private AddressDbRepository addressDBRepo;

    @Autowired
    private ClientDbRepository clientDBRepo;

    @Autowired
    private IClientService service;

    @BeforeEach
    public void setUp() {
        Address address = Address.Builder()
                .id(10)
                .city("LasVegas")
                .street("Roulette")
                .number(45)
                .build();
        addressDBRepo.save(address);

        c1 = Client.Builder()
                .id(10)
                .firstName("John")
                .lastName("Jackson")
                .address(address)
                .age(40)
                .build();
        c2 = Client.Builder()
                .id(20)
                .firstName("Abdul")
                .lastName("Johnson")
                .address(address)
                .age(20)
                .build();
        c3 = Client.Builder()
                .id(30)
                .firstName("Mike")
                .lastName("Jack")
                .address(address)
                .build();

        clientDBRepo.save(c1);
        clientDBRepo.save(c2);
        clientDBRepo.save(c3);
    }

    @AfterEach
    public void tearDown() {
        c1 = null;
        c2 = null;
        c3 = null;

        clientDBRepo.deleteAll();
        addressDBRepo.deleteAll();
    }

    @Test
    public void filterClientsByNameTest() {
        Set<Client> nameLikeJack = service.filterClientsByName("Jack");
        Set<Client> nameLikeJohn = service.filterClientsByName("John");

        assertTrue(nameLikeJack.contains(c1));
        assertTrue(nameLikeJack.contains(c3));

        assertTrue(nameLikeJohn.contains(c2));
        assertTrue(nameLikeJohn.contains(c1));
    }

    @Test
    public void filterClientsInAgeInterval() {
        Set<Client> clients = service.filterClientsInAgeInterval(30, 50);
        assertTrue(clients.contains(c1));
    }

}