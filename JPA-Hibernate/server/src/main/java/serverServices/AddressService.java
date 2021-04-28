package serverServices;

import domain.Address;
import domain.Validators.AddressValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repository.AddressDbRepository;
import services.IAddressService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Set<Address> result = getAll();
        List<Address> deletedAddresses = new ArrayList<>();
        result.stream()
                    .filter(address -> address.getNumber().equals(number))
                    .forEach(address -> {
                        deletedAddresses.add(address);
                        this.repository.deleteById(address.getId());
                    });
        log.info("deleteAddressWithNumber deletedAddresses={} - method finished", deletedAddresses);
    }

}
