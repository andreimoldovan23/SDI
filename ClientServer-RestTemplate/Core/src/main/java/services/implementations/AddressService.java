package services.implementations;

import domain.Address;
import domain.Validators.AddressValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.AddressDbRepository;
import services.interfaces.IAddressService;

@Slf4j
@org.springframework.stereotype.Service
public class AddressService extends Service<Integer, Address> implements IAddressService {

    /**
     * Constructor for AddressService
     * @param repository of type {@code Repository<Integer, Address>}
     */
    public AddressService(AddressDbRepository repository, AddressValidator addressValidator) {
        super(repository, addressValidator);
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

}
