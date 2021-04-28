package service;

import domain.Address;
import repository.Repository;
import services.IAddressService;

import java.util.HashSet;
import java.util.Set;

public class AddressService extends Service<Integer, Address> implements IAddressService {

    /**
     * Constructor for AddressService
     * @param repository of type {@code Repository<Integer, Address>}
     */
    public AddressService(Repository<Integer, Address> repository) {
        super(repository);
    }

    /**
     * Deletes all addresses with a given street number
     * @param number: integer
     */
    public void deleteAddressWithNumber(Integer number) {
        Iterable<Address> addresses = this.repository.findAll();
        Set<Address> result = new HashSet<>();
        addresses.forEach(result::add);
        result.stream()
                    .filter(address -> address.getNumber().equals(number))
                    .forEach(address -> this.repository.delete(address.getId()));
    }
}
