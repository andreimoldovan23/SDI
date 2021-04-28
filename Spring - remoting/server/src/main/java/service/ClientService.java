package service;

import domain.Client;
import repository.Repository;
import services.IClientService;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService extends Service<Integer, Client> implements IClientService {

    /**
     * Constructor for ClientService
     * @param repository of type {@code Repository<Integer, Client>}
     */
    public ClientService(Repository<Integer, Client> repository) {
        super(repository);
    }

    /**
     * Gets the clients that have a substring in their name in a set
     * @param name of type String
     * @return {@code Set<Client>}
    */
    public Set<Client> filterClientsByName(String name) {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .filter(client -> client.getFirstName().contains(name) ||
                        client.getLastName().contains(name)).collect(Collectors.toSet());
    }

    /**
     * Gets all clients with age in the given interval
     * @param age1 : low age interval limit
     * @param age2 : high age interval limit
     * @return {@code Set<Client>}
     */
    public Set<Client> filterClientsInAgeInterval(Integer age1, Integer age2) {
        return StreamSupport.stream(this.repository.findAll().spliterator(), false)
                .filter(c -> c.getAge() != null)
                .filter(c -> c.getAge() >= age1 && c.getAge() <= age2)
                .collect(Collectors.toSet());
    }

}
