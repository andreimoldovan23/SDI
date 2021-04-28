package services.interfaces;

import domain.Address;

public interface IAddressService extends ICrudService<Integer, Address> {
    void deleteAddressWithNumber(Integer number);
}
