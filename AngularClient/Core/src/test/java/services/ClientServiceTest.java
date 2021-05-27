package services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Address;
import domain.Client;
import domain.Validators.AddressValidatorException;
import domain.Validators.ClientValidatorException;
import domain.Validators.ValidatorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import repositories.addressFragments.AddressDbRepository;
import repositories.clientFragments.ClientDbRepository;
import services.interfaces.IClientService;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class ClientServiceTest {

    @Autowired
    private AddressDbRepository addressDBRepo;

    @Autowired
    private ClientDbRepository clientDBRepo;

    @Autowired
    private IClientService service;

    private Client findOne(Integer id) {
        return clientDBRepo.findByIdWithAddress(id).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
    }

    @Test
    public void filterClientsByNameTest() {
        Set<Client> nameLikeJack = service.filterClientsByName("Johnson");
        assertEquals(nameLikeJack.size(), 3);
        nameLikeJack.forEach(client -> assertNotNull(client.getAddress()));
    }

    @Test
    public void filterClientsInAgeInterval() {
        Set<Client> clients = service.filterClientsInAgeInterval(40, 50);
        assertEquals(clients.size(), 3);
        clients.forEach(client -> assertNotNull(client.getAddress()));
    }

    @Test
    public void getAllTest() {
        Set<Client> clients = service.getAll();
        assertEquals(clients.size(), 5);
        clients.forEach(client -> assertNotNull(client.getAddress()));
    }

    @Test
    public void deleteTest() {
        service.Delete(1);
        assertEquals(service.getAll().size(), 4);
    }

    @Test(expected = ValidatorException.class)
    public void deleteBadIdTest() {
        service.Delete(1000);
    }

    @Test
    public void updateTest() {
        Client c1 = findOne(1);
        c1.setAge(30);
        service.Update(c1);
        assertEquals(findOne(1).getAge(), Integer.valueOf(30));
    }

    @Test(expected = ValidatorException.class)
    public void updateBadIdTest() {
        Client c1 = findOne(1);
        c1.setId(1000);
        service.Update(c1);
    }

    @Test(expected = ValidatorException.class)
    public void updateNotValidTest() {
        Client c1 = findOne(1);
        c1.setFirstName("Mick ?Rory");

        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setFirstName("Mick Rory");
            c1.setAge(12);
        }

        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setAge(102);
        }

        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setAge(34);
            c1.setPhoneNumber("00231231131");
        }

        try {
            service.Update(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setPhoneNumber("0756823468");
            c1.getAddress().setNumber(-3);
        }

        c1.getAddress().setNumber(3);
        c1.getAddress().setId(1000);
        service.Update(c1);
    }

    @Test
    public void updateNewAddressTest() {
        Address address2 = Address.builder()
                .city("Los Angeles")
                .street("Casino")
                .number(100)
                .build();
        address2 = addressDBRepo.save(address2);

        Client c1 = findOne(1);
        c1.setAddress(address2);
        service.Update(c1);
        assertEquals(findOne(1).getAddress(), address2);
    }

    @Test(expected = ValidatorException.class)
    public void addAlreadyExistentTest() {
        Client c1 = findOne(1);
        service.Add(c1);
    }

    @Test(expected = ValidatorException.class)
    public void addNotValidTest() {
        Client c1 = findOne(1);
        c1.setId(null);
        c1.setFirstName("Mick ?Rory");

        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setFirstName("Mick Rory");
            c1.setAge(12);
        }

        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setAge(102);
        }

        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setAge(34);
            c1.setPhoneNumber("00231231131");
        }

        try {
            service.Add(c1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            c1.setPhoneNumber("0756823468");
            c1.getAddress().setNumber(-3);
        }

        c1.getAddress().setNumber(3);
        c1.getAddress().setId(1000);
        service.Add(c1);
    }

    @Test
    public void addClient() {
        Client client = Client.builder().firstName("Mircea").lastName("Popescu").build();
        client.setAge(50);
        client.setPhoneNumber("+40756823468");
        client.setAddress(addressDBRepo.findById(1).orElseThrow(() -> new AddressValidatorException("Invalid address id")));
        service.Add(client);
        client.setId(6);
        assertEquals(findOne(6), client);
    }

}