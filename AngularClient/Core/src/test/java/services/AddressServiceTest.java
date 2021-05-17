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
import org.springframework.test.context.ContextConfiguration;
import repositories.AddressDbRepository;
import services.interfaces.IAddressService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ContextConfiguration(classes = JpaConfig.class)
public class AddressServiceTest {

    private Address a1, a2, a3;

    @Autowired
    private AddressDbRepository addressDbRepository;

    @Autowired
    private IAddressService service;

    @BeforeEach
    public void setUp() {
        a1 = Address.builder()
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        a2 = Address.builder()
                .city("cccc")
                .street("dddd")
                .number(2)
                .build();
        a3 = Address.builder()
                .city("eeee")
                .street("ffff")
                .number(1)
                .build();

        service.Add(a1);
        service.Add(a2);
        service.Add(a3);

        List<Address> addresses = addressDbRepository.findAll();
        a1 = addresses.get(0);
        a2 = addresses.get(1);
        a3 = addresses.get(2);
    }

    @AfterEach
    public void tearDown() {
        a1 = null;
        a2 = null;
        a3 = null;

        addressDbRepository.deleteAll();
    }

    @Test
    public void deleteAddressByNumber() {
        service.deleteAddressWithNumber(1);
        Set<Address> addresses = service.getAll();
        assertEquals(addresses.size(), 1);
    }

    @Test
    public void getAllTest() {
        Set<Address> addresses = service.getAll();
        assertEquals(addresses.size(), 3);
    }

    @Test
    public void getOneTest() {
        Address address = service.findOne(a1.getId());
        assertEquals(address, a1);
    }

    @Test
    public void getOneBadTest() {
        assertThrows(ValidatorException.class, () -> service.findOne(1000));
    }

    @Test
    public void deleteTest() {
        service.Delete(a1.getId());
        assertEquals(service.getAll().size(), 2);
    }

    @Test
    public void deleteBadIdTest() {
        assertThrows(ValidatorException.class, () -> service.Delete(1000));
        assertEquals(service.getAll().size(), 3);
    }

    @Test
    public void updateTest() {
        a1.setNumber(45);
        service.Update(a1);
        assertEquals(service.findOne(a1.getId()).getNumber(), 45);
    }

    @Test
    public void updateBadIdTest() {
        a1.setId(200);
        assertThrows(ValidatorException.class, () -> service.Update(a1));
    }

    @Test
    public void updateNotValidTest() {
        a1.setCity(null);
        assertThrows(ValidatorException.class, () -> service.Update(a1));

        a1.setCity("Los- Mandingas");
        assertThrows(ValidatorException.class, () -> service.Update(a1));

        a1.setCity("Los Mandingas");
        a1.setNumber(-3);
        assertThrows(ValidatorException.class, () -> service.Update(a1));
    }

    @Test
    public void addAlreadyExistentTest() {
        assertThrows(ValidatorException.class, () -> service.Add(a1));
    }

    @Test
    public void addInvalidTest() {
        a1.setId(null);

        a1.setCity(null);
        assertThrows(ValidatorException.class, () -> service.Add(a1));

        a1.setCity("Los- Mandingas");
        assertThrows(ValidatorException.class, () -> service.Add(a1));

        a1.setCity("Los Mandingas");
        a1.setNumber(-3);
        assertThrows(ValidatorException.class, () -> service.Add(a1));
    }

    @Test
    public void howManyClientsTest() {
        Client c1 = Client.builder()
                .firstName("Josh")
                .lastName("Popescu")
                .address(a1)
                .build();

        Client c2 = Client.builder()
                .firstName("Josh")
                .lastName("Popescu")
                .address(a2)
                .build();

        Client c3 = Client.builder()
                .firstName("Mike")
                .lastName("Tyson")
                .address(a1)
                .build();

        a1.getClients().add(c1);
        a1.getClients().add(c3);
        a2.getClients().add(c2);

        a1 = addressDbRepository.save(a1);
        a2 = addressDbRepository.save(a2);

        assertEquals(service.howManyClientsLiveHere(a1.getId()), 2);
        assertEquals(service.howManyClientsLiveHere(a2.getId()), 1);
        assertThrows(ValidatorException.class, () -> service.howManyClientsLiveHere(100));
    }

}
