package clientServices;

import domain.Client;
import domain.Validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import services.IClientService;

import java.util.Set;

@org.springframework.stereotype.Service
public class ClientService extends Service<Integer, Client> implements IClientService {

    @Autowired
    private IClientService clientService;

    /**
     * Gets the clients that have a substring in their name in a set
     * @param name of type String
     * @return {@code Set<Client>} a set of clients
    */
    public Set<Client> filterClientsByName(String name) {
        return clientService.filterClientsByName(name);
    }

    /**
     * Gets all clients with age in the given interval
     * @param age1: low age interval limit
     * @param age2: high age interval limit
     * @return {@code Set<Client>} a set of clients
     */
    public Set<Client> filterClientsInAgeInterval(Integer age1, Integer age2) {
        return clientService.filterClientsInAgeInterval(age1, age2);
    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Client element) {
        clientService.Add(element);
    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Client element) {
        clientService.Update(element);
    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    @Override
    public void Delete(Integer id) {
        clientService.Delete(id);
    }

    /**
     * Gets all entities in a set
     * @return {@code Set<entity>}
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public Set<Client> getAll() {
        return clientService.getAll();
    }

    @Override
    public Client findOneById(Integer integer) throws ValidatorException {
        return clientService.findOneById(integer);
    }

}
