package services.implementations;

import domain.Address;
import domain.Validators.AddressValidator;
import domain.Validators.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.AddressDbRepository;
import repositories.ClientDbRepository;
import services.interfaces.IAddressService;

@Slf4j
@org.springframework.stereotype.Service
public class AddressService extends Service<Integer, Address> implements IAddressService {

    private final ClientDbRepository clientDbRepository;

    /**
     * Constructor for AddressService
     * @param repository of type {@code Repository<Integer, Address>}
     */
    public AddressService(AddressDbRepository repository, AddressValidator addressValidator,
                          ClientDbRepository clientDbRepository) {
        super(repository, addressValidator);
        this.clientDbRepository = clientDbRepository;
    }

    /**
     * Deletes all addresses with a given street number
     * @param number: integer
     */
    @Transactional
    public void deleteAddressWithNumber(Integer number) {
        log.info("deleteAddressWithNumber - method entered - number={}", number);
        ((AddressDbRepository)this.repository).deleteAddressByNumber(number);
        log.info("deleteAddressWithNumber - method finished");
    }

    /**
     * Updates an element
     * @param element of type address
     */
    @Transactional
    public void Update(Address element) throws ValidatorException {
        log.info("update address - method entered");
        validator.validate(element);
        repository.findById(element.getId()).ifPresentOrElse(elem ->
        {
            elem.setNumber(element.getNumber());
            elem.setCity(element.getCity());
            elem.setStreet(element.getStreet());
            repository.save(elem);
        }, () -> {
            log.info("update address - throwing exception");
            throw new ValidatorException("No such element");
        });
        log.info("update address - method finished");
    }

    /**
     * Gets the number of clients that live at that address
     * @param id : the id of the address
     * @return Integer : the number of clients living there
     */
    @Transactional
    public Integer howManyClientsLiveHere(Integer id) {
        log.info("howManyClientsLiveHere - method entered - id={}", id);
        Address address = this.findOne(id);
        return this.clientDbRepository.countClientsLivingAtAddress(address.getId());
    }

}
