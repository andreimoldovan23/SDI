package services.implementations;

import domain.Address;
import domain.Validators.AddressValidator;
import domain.Validators.AddressValidatorException;
import domain.Validators.ValidatorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import services.interfaces.IAddressService;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressDbRepository addressDbRepository;
    private final ClientDbRepository clientDbRepository;
    private final AddressValidator addressValidator;

    /**
     * Deletes all addresses with a given street number
     * @param number: integer
     */
    @Transactional
    public void deleteAddressWithNumber(Integer number) {
        log.info("deleteAddressWithNumber - method entered - number={}", number);
        addressDbRepository.deleteAddressByNumber(number);
        log.info("deleteAddressWithNumber - method finished");
    }

    /**
     * Adds an address
     * @param entity: Address (to be added)
     * @throws ValidatorException
     *      if the address is not valid or if it already exists
     */
    @Transactional
    @Override
    public void Add(Address entity) throws ValidatorException {
        log.info("add address {}, {}, {}", entity.getCity(), entity.getStreet(), entity.getNumber());
        addressValidator.validate(entity);
        try {
            findOne(entity.getId());
            throw new ValidatorException("Entity already exists");
        } catch (AddressValidatorException ae) {
            addressDbRepository.save(entity);
        }
    }

    /**
     * Updates an address
     * @param element: Address (to be updated)
     * @throws ValidatorException
     *          if the address is not valid or if it doesn't exist
     */
    @Transactional
    @Override
    public void Update(Address element) throws ValidatorException {
        log.info("update address w/ id {} - method entered", element.getId());
        addressValidator.validate(element);
        Address address = findOne(element.getId());
        address.setNumber(element.getNumber());
        address.setCity(element.getCity());
        address.setStreet(element.getStreet());
        addressDbRepository.save(address);
        log.info("update address - method finished");
    }

    /**
     * Deletes an address by id
     * @param id: Integer, id of address
     * @throws ValidatorException
     *          if the address does not exist
     */
    @Transactional
    @Override
    public void Delete(Integer id) throws ValidatorException {
        log.info("delete address w/ id {} - method entered", id);
        Address address = findOne(id);
        addressDbRepository.delete(address);
    }

    /**
     * Returns a set of all addresses
     * @return {@code Set<Address>}
     */
    @Override
    public Set<Address> getAll() {
        log.info("getAll address - method entered");
        return new HashSet<>(addressDbRepository.findAll());
    }

    /**
     * Returns an address by its id
     * @param id: Integer, id of address
     * @return Address
     * @throws ValidatorException
     *      if the address does not exist or id is null
     */
    @Override
    public Address findOne(Integer id) throws ValidatorException {
        log.info("findOne w/ id {} address - method entered", id);
        if (id == null) throw new AddressValidatorException("Invalid address id");
        return addressDbRepository.findById(id).orElseThrow(() -> new AddressValidatorException("Invalid address id"));
    }

    /**
     * Gets the number of clients that live at that address
     * @param id : the id of the address
     * @return Integer : the number of clients living there
     */
    @Transactional
    public Integer howManyClientsLiveHere(Integer id) {
        log.info("howManyClientsLiveHere - method entered - id={}", id);
        return clientDbRepository.countClientsLivingAtAddress(findOne(id).getId());
    }

}
