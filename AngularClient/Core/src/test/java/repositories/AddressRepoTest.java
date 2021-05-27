package repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Address;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class AddressRepoTest {

    @Autowired
    private AddressDbRepository addressDbRepository;

    @Autowired
    private ClientDbRepository clientDbRepository;

    @Test
    public void testFindAllWithClients() {
        Set<Address> addresses = addressDbRepository.findAllWithClients();
        addresses.forEach(address -> {
                assertTrue(address.getClients().size() > 0);
        });
    }

    @Test
    public void testFindByIdWithClients() {
        Address address = addressDbRepository.findByIdWithClients(1);

        assertEquals(address.getCity(), "Moscow");
        assertEquals(address.getClients().size(), 2);

        List<String> clientNames = address.getClients().stream()
                .map(client -> client.getFirstName() + " " + client.getLastName())
                .collect(Collectors.toList());

        assertTrue(clientNames.contains("Mike Tyson"));
        assertTrue(clientNames.contains("Mike Johnson"));
    }

    @Test
    public void testDeleteByNumber() {
        addressDbRepository.deleteAddressByNumber(10);

        int numberAddresses = addressDbRepository.findAll().size();
        int numberClients = clientDbRepository.findAll().size();
        int numberOrders = Math.toIntExact(clientDbRepository.findAllWithOrders().stream()
                .mapToLong(client -> client.getOrders().size())
                .sum());

        assertEquals(numberAddresses, 2);
        assertEquals(numberClients, 2);
        assertEquals(numberOrders, 3);
    }

}
