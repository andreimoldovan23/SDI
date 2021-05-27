package services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Address;
import domain.Validators.AddressValidatorException;
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
import services.interfaces.IAddressService;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class AddressServiceTest {

    @Autowired
    private AddressDbRepository addressDbRepository;

    @Autowired
    private IAddressService service;

    private Address findOne(Integer id) {
        return addressDbRepository.findById(id).orElseThrow(() -> new AddressValidatorException("Invalid address id"));
    }

    @Test
    public void addAddress() {
        Address address = Address.builder().city("Cluj").street("Dorobantilor").number(45).build();
        service.Add(address);
        address.setId(5);
        assertEquals(findOne(5), address);
    }

    @Test
    public void deleteAddressByNumber() {
        service.deleteAddressWithNumber(10);
        Set<Address> addresses = service.getAll();
        assertEquals(addresses.size(), 2);
    }

    @Test
    public void getAllTest() {
        Set<Address> addresses = service.getAll();
        assertEquals(addresses.size(), 4);
    }

    @Test
    public void deleteTest() {
        service.Delete(1);
        assertEquals(service.getAll().size(), 3);
    }

    @Test(expected = ValidatorException.class)
    public void deleteBadIdTest() {
        service.Delete(1000);
    }

    @Test
    public void updateTest() {
        Address a1 = findOne(1);
        a1.setNumber(100);
        service.Update(a1);
        assertEquals(findOne(1).getNumber(), Integer.valueOf(100));
    }

    @Test(expected = ValidatorException.class)
    public void updateBadIdTest() {
        Address a1 = findOne(1);
        a1.setId(200);
        service.Update(a1);
    }

    @Test(expected = ValidatorException.class)
    public void updateNotValidTest() {
        Address a1 = findOne(1);
        a1.setCity(null);

        try {
            service.Update(a1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            a1.setCity("Los- Mandingas");
        }

        try {
            service.Update(a1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            a1.setCity("Los Mandingas");
        }

        a1.setNumber(-3);
        service.Update(a1);
    }

    @Test(expected = ValidatorException.class)
    public void addAlreadyExistentTest() {
        Address a1 = findOne(1);
        service.Add(a1);
    }

    @Test(expected = ValidatorException.class)
    public void addInvalidTest() {
        Address a1 = Address.builder().city("Cluj").street("Dorobantilor").number(45).build();

        a1.setCity(null);
        try {
            service.Add(a1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            a1.setCity("Los- Mandings");
        }

        try {
            service.Add(a1);
            throw new RuntimeException();
        } catch (ValidatorException ve) {
            a1.setCity("Los Mandingas");
        }

        a1.setNumber(-3);
        service.Add(a1);
    }

    @Test(expected = ValidatorException.class)
    public void howManyClientsTest() {
        assertEquals(service.howManyClientsLiveHere(1), Integer.valueOf(2));
        assertEquals(service.howManyClientsLiveHere(2), Integer.valueOf(1));
        service.howManyClientsLiveHere(100);
    }

}
