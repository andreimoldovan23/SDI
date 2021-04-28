package serverServices;

import domain.Client;
import domain.Validators.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repository.ClientDbRepository;
import services.IClientService;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
public class ClientService extends Service<Integer, Client> implements IClientService {

    /**
     * Constructor for ClientService
     * @param repository of type {@code Repository<Integer, Client>}
     */
    public ClientService(ClientDbRepository repository, ClientValidator clientValidator) {
        super(repository, clientValidator);
    }

    /**
     * Gets the clients that have a substring in their name in a set
     * @param name of type String
     * @return {@code Set<Client>}
    */
    @Transactional
    public Set<Client> filterClientsByName(String name) {
        log.info("filterClientsByName - method entered - name={}", name);
        Set<Client> result = this.repository.findAll().stream()
                .filter(client -> client.getFirstName().contains(name) ||
                        client.getLastName().contains(name)).collect(Collectors.toSet());
        log.info("filterClientsByName result = {} - method finished", result);
        return result;
    }

    /**
     * Gets all clients with age in the given interval
     * @param age1 : low age interval limit
     * @param age2 : high age interval limit
     * @return {@code Set<Client>}
     */
    @Transactional
    public Set<Client> filterClientsInAgeInterval(Integer age1, Integer age2) {
        log.info("filterClientsInAgeInterval - method entered - age1={}, age2={}", age1, age2);
        Set<Client> result = this.repository.findAll().stream()
                .filter(c -> c.getAge() != null)
                .filter(c -> c.getAge() >= age1 && c.getAge() <= age2)
                .collect(Collectors.toSet());
        log.info("filterClientsInAgeInterval result = {} - method finished", result);
        return result;
    }

}
