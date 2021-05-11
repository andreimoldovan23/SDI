package mappers;

import domain.Address;
import dtos.AddressDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddressMapperTest extends AbstractMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void addressToDTO() {
        address.setId(addressId);
        AddressDTO dto = addressMapper.addressToDTO(address);

        assertEquals(dto.getId(), addressId);
        assertEquals(dto.getCity(), addressCity);
        assertEquals(dto.getStreet(), addressStreet);
        assertEquals(dto.getNumber(), addressNumber);
    }

    @Test
    public void dtoToAddress() {
        Address address = addressMapper.DTOtoAddress(addressDto);

        assertEquals(address.getId(), addressId);
        assertEquals(address.getCity(), addressCity);
        assertEquals(address.getStreet(), addressStreet);
        assertEquals(address.getNumber(), addressNumber);
    }

}
