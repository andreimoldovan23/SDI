package services;

import domain.Address;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class AddressService extends Service<Integer, Address> implements IAddressService {

    private final IAddressService addressService;

    /**
     * Constructor for AddressService
     */
    public AddressService (IAddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Deletes all addresses with a given street number
     * @param number: integer
     */
    public void deleteAddressWithNumber(Integer number) {
        addressService.deleteAddressWithNumber(number);
    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Address element) {
        addressService.Add(element);
    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Address element) {
        addressService.Update(element);
    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    @Override
    public void Delete(Integer id) {
        addressService.Delete(id);
    }

    /**
     * Gets all entities in a set
     * @return {@code Set<entity>}
     */
    @Override
    public Set<Address> getAll() {
        return addressService.getAll();
    }

}
