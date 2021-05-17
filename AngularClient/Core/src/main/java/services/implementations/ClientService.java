package services.implementations;

import domain.Address;
import domain.Client;
import domain.ShopOrder;
import domain.Validators.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import services.interfaces.IClientService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientDbRepository clientDbRepository;
    private final AddressDbRepository addressDbRepository;
    private final ClientValidator clientValidator;
    private final AddressValidator addressValidator;

    /**
     * Gets a set of the clients that have a certain substring in their name
     * @param name: String
     * @return {@code Set<Client>}
    */
    @Transactional
    public Set<Client> filterClientsByName(String name) {
        log.info("filterClientsByName - method entered - name={}", name);
        Set<Client> result = clientDbRepository.filterClientByFirstNameOrLastName(name);
        log.info("filterClientsByName result = {} - method finished", result);
        return result;
    }

    /**
     * Gets a set of all clients with their age in the given interval
     * @param age1 : Integer,  low age interval limit
     * @param age2 : Integer,  high age interval limit
     * @return {@code Set<Client>}
     */
    @Transactional
    public Set<Client> filterClientsInAgeInterval(Integer age1, Integer age2) {
        log.info("filterClientsInAgeInterval - method entered - age1={}, age2={}", age1, age2);
        Set<Client> result = clientDbRepository.filterClientByAgeInterval(age1, age2);
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
        return findOne(id).getOrders().stream()
                .map(ShopOrder::getCoffee)
                .collect(Collectors.toSet())
                .size();
    }

    /**
     * Adds a client
     * @param entity: Client (to be added)
     * @throws ValidatorException
     *      if the client is not valid, if the address of the client is not valid or if it already exists
     */
    @Transactional
    @Override
    public void Add(Client entity) throws ValidatorException {
        log.info("add client w/ {}, {} - method entered", entity.getFirstName(), entity.getLastName());
        clientValidator.validate(entity);
        addressValidator.validate(entity.getAddress());
        try {
            findOne(entity.getId());
            throw new ValidatorException("Entity already exists");
        } catch (ClientValidatorException ce) {
            Address address = addressDbRepository.findById(entity.getAddress().getId())
                    .orElseThrow(() -> new AddressValidatorException("Invalid address"));
            address.getClients().add(entity);
            addressDbRepository.save(address);
        }
    }

    /**
     * Updates a client
     * @param element: Client (to be updated)
     * @throws ValidatorException
     *          if the client is not valid, if the address of the client is not valid or if it doesn't exist
     */
    @Transactional
    @Override
    public void Update(Client element) throws ValidatorException {
        log.info("update client w/ id {} - method entered", element.getId());
        clientValidator.validate(element);
        addressValidator.validate(element.getAddress());
        Client client = findOne(element.getId());
        Address addr = addressDbRepository.findById(element.getAddress().getId())
                    .orElseThrow(() -> new AddressValidatorException("Invalid address"));
        client.setFirstName(element.getFirstName());
        client.setLastName(element.getLastName());
        client.setAge(element.getAge());
        client.setPhoneNumber(element.getPhoneNumber());
        if (!addr.equals(client.getAddress())) {
            Address oldAddress = client.getAddress();
            oldAddress.getClients().remove(client);
            addressDbRepository.save(oldAddress);

            client.setAddress(addr);
            addr.getClients().add(client);
            addressDbRepository.save(addr);
        } else {
            clientDbRepository.save(client);
        }
        log.info("update client - method finished");
    }

    /**
     * Deletes a client by id
     * @param id: Integer, id of client
     * @throws ValidatorException
     *          if the client does not exist
     */
    @Transactional
    @Override
    public void Delete(Integer id) throws ValidatorException {
        log.info("delete client w/ id {} - method entered", id);
        Client client = findOne(id);
        Address addr = client.getAddress();
        addr.getClients().remove(client);
        clientDbRepository.delete(client);
        addressDbRepository.save(addr);
    }

    /**
     * Returns a set of all clients
     * @return {@code Set<Client>}
     */
    @Override
    public Set<Client> getAll() {
        log.info("getAll clients - method entered");
        return new HashSet<>(clientDbRepository.findAll());
    }

    /**
     * Returns a client by its id
     * @param id: Integer, id of client
     * @return Client
     * @throws ValidatorException
     *      if the client does not exist or id is null
     */
    @Override
    public Client findOne(Integer id) throws ValidatorException {
        log.info("findOne clients w/ id {} - method entered", id);
        if (id == null) throw new ClientValidatorException("Invalid client id");
        return clientDbRepository.findById(id).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
    }

}
