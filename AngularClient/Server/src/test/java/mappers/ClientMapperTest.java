package mappers;

import domain.Address;
import domain.Client;
import dtos.AddressDTO;
import dtos.ClientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMapperTest extends AbstractMapperTest {

    @Autowired
    private ClientMapper clientMapper;

    @Test
    public void clientToDTO() {
        address.setId(addressId);
        client.setId(clientId);
        client.setAge(clientAge);
        client.setPhoneNumber(clientPhoneNumber);
        ClientDTO dto = clientMapper.clientToDTO(client);
        AddressDTO dtoAddr = dto.getAddress();

        assertEquals(dto.getId(), clientId);
        assertEquals(dto.getFirstName(), clientFirstName);
        assertEquals(dto.getLastName(), clientLastName);
        assertEquals(dto.getAge(), clientAge);
        assertEquals(dto.getPhoneNumber(), clientPhoneNumber);

        assertEquals(dtoAddr.getId(), addressId);
        assertEquals(dtoAddr.getCity(), addressCity);
        assertEquals(dtoAddr.getStreet(), addressStreet);
        assertEquals(dtoAddr.getNumber(), addressNumber);
    }

    @Test
    public void dtoToClient() {
        Client entity = clientMapper.DTOtoClient(clientDto);
        Address entityAddr = entity.getAddress();

        assertEquals(entity.getId(), clientId);
        assertEquals(entity.getFirstName(), clientFirstName);
        assertEquals(entity.getLastName(), clientLastName);
        assertEquals(entity.getAge(), clientAge);
        assertEquals(entity.getPhoneNumber(), clientPhoneNumber);

        assertEquals(entityAddr.getId(), addressId);
        assertEquals(entityAddr.getCity(), addressCity);
        assertEquals(entityAddr.getStreet(), addressStreet);
        assertEquals(entityAddr.getNumber(), addressNumber);
    }

}
