package services;

import config.JpaConfig;
import domain.Address;
import domain.Client;
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
import services.interfaces.IClientService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
        Address address = Address.builder()
                .city("LasVegas")
                .street("Roulette")
                .number(45)
                .build();
        addressDBRepo.save(address);

        c1 = Client.builder()
                .firstName("John")
                .lastName("Jackson")
                .address(address)
                .age(40)
                .build();
        c2 = Client.builder()
                .firstName("Abdul")
                .lastName("Johnson")
                .address(address)
                .age(20)
                .build();
        c3 = Client.builder()
                .firstName("Mike")
                .lastName("Jack")
                .address(address)
                .build();

        clientDBRepo.save(c1);
        clientDBRepo.save(c2);
        clientDBRepo.save(c3);

        List<Client> clients = clientDBRepo.findAll();
        c1 = clients.get(0);
        c2 = clients.get(1);
        c3 = clients.get(2);
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

    @Test
    public void getAllTest() {
        Set<Client> clients = service.getAll();
        assertEquals(clients.size(), 3);
    }

    @Test
    public void getOneTest() {
        Client client = service.findOne(c1.getId());
        assertEquals(client, c1);
    }

    @Test
    public void getOneBadTest() {
        assertThrows(ValidatorException.class, () -> service.findOne(200));
    }

    @Test
    public void deleteTest() {
        assertThrows(DataAccessException.class, () -> service.Delete(200));
        assertEquals(service.getAll().size(), 3);

        service.Delete(c1.getId());
        assertEquals(service.getAll().size(), 2);
    }

    @Test
    public void updateTest() {
        c1.setAge(45);

        service.Update(c1);
        assertEquals(service.findOne(c1.getId()).getAge(), 45);

        c1.setId(200);
        assertThrows(ValidatorException.class, () -> service.Update(c1));
    }

}