package repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import config.ITConfig;
import domain.Client;
import domain.Validators.ClientValidatorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import repositories.clientFragments.ClientDbRepository;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ITConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/db-data.xml")
public class ClientRepoTest {

    @Autowired
    private ClientDbRepository clientDbRepository;

    @Test
    public void testCountClientsLivingAtAddress() {
        assertEquals(clientDbRepository.countClientsLivingAtAddress(1), Integer.valueOf(2));
    }

    @Test
    public void testFindAllWithAddresses() {
        clientDbRepository.findAllWithAddresses().forEach(client -> assertNotNull(client.getAddress()));
    }

    @Test
    public void testFindByIdWithAddress() {
        Client client = clientDbRepository.findByIdWithAddress(1).orElseThrow(() -> new ClientValidatorException("Invalid client id"));

        assertEquals(client.getFirstName(), "Mike");
        assertEquals(client.getLastName(), "Tyson");
        assertEquals(client.getAddress().getId(), Integer.valueOf(1));
        assertEquals(client.getAddress().getCity(), "Moscow");
        assertEquals(client.getAddress().getClients().size(), 2);
    }

    @Test
    public void testFindByIdWithOrders() {
        Client client = clientDbRepository.findByIdWithOrders(1).orElseThrow(() -> new ClientValidatorException("Invalid client id"));

        assertEquals(client.getFirstName(), "Mike");
        assertEquals(client.getLastName(), "Tyson");
        assertEquals(client.getOrders().size(), 4);
        client.getOrders().forEach(shopOrder -> {
            assertEquals(shopOrder.getClient(), client);
            assertNotNull(shopOrder.getCoffee());
        });
    }

    @Test
    public void testFindAllWithOrders() {
        clientDbRepository.findAllWithOrders().forEach(client -> client.getOrders().forEach(shopOrder -> {
            assertEquals(shopOrder.getClient(), client);
            assertNotNull(shopOrder.getCoffee());
        }));
    }

    @Test
    public void testFilterByName() {
        Set<Client> clients = clientDbRepository.filterClientByFirstNameOrLastName("Johnson");

        assertEquals(clients.size(), 3);
        clients.forEach(client -> assertNotNull(client.getAddress()));
    }

    @Test
    public void testFilterAge() {
        Set<Client> clients = clientDbRepository.filterClientByAgeInterval(40, 50);

        assertEquals(clients.size(), 3);
        clients.forEach(client -> assertNotNull(client.getAddress()));
    }

}
