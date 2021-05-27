package services.implementations;

import domain.Address;
import domain.Client;
import domain.ShopOrder;
import domain.Validators.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import repositories.addressFragments.AddressDbRepository;
import repositories.clientFragments.ClientDbRepository;
import services.interfaces.IClientService;

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
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
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
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Integer howManyCoffeesClientOrdered(Integer id) {
        log.info("howManyCoffeesClientOrdered - method entered - id={}", id);
        return findOneWithOrders(id).getOrders().stream()
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

        Address address = addressDbRepository.findByIdWithClients(entity.getAddress().getId());
        if (address == null) throw new AddressValidatorException("Invalid address");

        address.getClients().stream()
                .filter(client -> client.getId().equals(entity.getId()))
                .findFirst().ifPresent(clie -> {
                    throw new ClientValidatorException("Client already exists");
        });

        address.getClients().add(entity);
        entity.setAddress(address);
        addressDbRepository.save(address);
    }

    /**
     * Updates a client
     * @param element: Client (to be updated)
     * @throws ValidatorException
     *          if the client is not valid, if the address of the client is not valid or if it doesn't exist
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void Update(Client element) throws ValidatorException {
        log.info("update client w/ id {} - method entered", element.getId());

        clientValidator.validate(element);
        addressValidator.validate(element.getAddress());

        Client client = findOneWithAddress(element.getId());

        Address addr = addressDbRepository.findByIdWithClients(element.getAddress().getId());
        if (addr == null) throw new AddressValidatorException("Invalid address");

        addr.getClients().stream()
                .filter(client1 -> client1.getId().equals(element.getId()))
                .findFirst()
                .ifPresentOrElse(client1 -> {
                    client.setFirstName(element.getFirstName());
                    client.setLastName(element.getLastName());
                    client.setAge(element.getAge());
                    client.setPhoneNumber(element.getPhoneNumber());
                    clientDbRepository.save(client);
                }, () -> {
                    Address oldAddress = client.getAddress();
                    oldAddress.getClients().remove(client);
                    addressDbRepository.save(oldAddress);

                    addr.getClients().add(client);
                    client.setAddress(addr);
                    addressDbRepository.save(addr);
                });

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

        Client client = findOneWithAddress(id);
        Address addr = client.getAddress();
        addr.getClients().remove(client);
        addressDbRepository.save(addr);
    }

    /**
     * Returns a set of all clients
     * @return {@code Set<Client>}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<Client> getAll() {
        log.info("getAll clients - method entered");
        return clientDbRepository.findAllWithAddresses();
    }

    /**
     * Returns a client by its id, with his associated address
     * @param id: Integer, id of client
     * @return Client
     * @throws ValidatorException
     *      if the client does not exist or id is null
     */
    private Client findOneWithAddress(Integer id) throws ValidatorException {
        log.info("findOneWithAddress clients w/ id {} - method entered", id);
        if (id == null) throw new ClientValidatorException("Invalid client id");
        return clientDbRepository.findByIdWithAddress(id).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
    }

    /**
     * Returns a client by its id, with his associated orders
     * @param id: Integer, id of client
     * @return Client
     * @throws ValidatorException
     *      if the client does not exist or id is null
     */
    private Client findOneWithOrders(Integer id) throws ValidatorException {
        log.info("findOneWithOrders clients w/ id {} - method entered", id);
        if (id == null) throw new ClientValidatorException("Invalid client id");
        return clientDbRepository.findByIdWithOrders(id).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
    }
}
