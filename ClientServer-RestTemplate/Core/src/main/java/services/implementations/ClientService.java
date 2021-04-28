package services.implementations;

import domain.Client;
import domain.Validators.ClientValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientDbRepository;
import services.interfaces.IClientService;

import java.util.Set;

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
        Set<Client> result = ((ClientDbRepository)this.repository).filterClientByFirstNameOrLastName(name);
        System.out.println(result);
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
        Set<Client> result = ((ClientDbRepository)this.repository).filterClientByAgeInterval(age1, age2);
        log.info("filterClientsInAgeInterval result = {} - method finished", result);
        return result;
    }

}
