package services.implementations;

import domain.Address;
import domain.Client;
import domain.Validators.ClientValidator;
import domain.Validators.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import repositories.OrderDbRepository;
import services.interfaces.IClientService;

import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
public class ClientService extends Service<Integer, Client> implements IClientService {

    private final OrderDbRepository orderDbRepository;
    private final AddressDbRepository addressDbRepository;

    /**
     * Constructor for ClientService
     * @param repository of type {@code Repository<Integer, Client>}
     */
    public ClientService(ClientDbRepository repository, ClientValidator clientValidator,
                         OrderDbRepository orderDbRepository, AddressDbRepository addressDbRepository) {
        super(repository, clientValidator);
        this.orderDbRepository = orderDbRepository;
        this.addressDbRepository = addressDbRepository;
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

    /**
     * Gets the number of coffees ordered by a client
     * @param id : id of the client
     * @return Integer : the number of coffees
     */
    @Transactional
    public Integer howManyCoffeesClientOrdered(Integer id) {
        log.info("howManyCoffeesClientOrdered - method entered - id={}", id);
        Client client = this.findOne(id);
        return this.orderDbRepository.howManyCoffeesClientOrdered(client.getId());
    }

    /**
     * Updates an element
     * @param element of type client
     */
    @Transactional
    public void Update(Client element) throws ValidatorException {
        log.info("update client - method entered");
        validator.validate(element);
        repository.findById(element.getId()).ifPresentOrElse(elem ->
        {
            Address addr = addressDbRepository.findById(element.getAddress().getId()).orElse(null);
            elem.setFirstName(element.getFirstName());
            elem.setLastName(element.getLastName());
            elem.setAge(element.getAge());
            elem.setPhoneNumber(element.getPhoneNumber());
            if (addr != null) elem.setAddress(addr);
            repository.save(elem);
        }, () -> {
            log.info("update client - throwing exception");
            throw new ValidatorException("No such element");
        });
        log.info("update client - method finished");
    }

}
