package mappers;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.ShopOrder;
import dtos.AddressDTO;
import dtos.ClientDTO;
import dtos.CoffeeDTO;
import dtos.ShopOrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopOrderMapperTest extends AbstractMapperTest {

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Test
    public void orderToDto() {
        ShopOrderDTO dto = shopOrderMapper.shopOrderToDTO(order);
        ClientDTO dtoClient = dto.getClient();
        CoffeeDTO dtoCoffee = dto.getCoffee();
        AddressDTO dtoClientAddr = dtoClient.getAddress();

        assertEquals(dto.getId(), orderId);
        assertEquals(dto.getStatus(), orderStatus);
        assertEquals(dto.getTime(), time);

        assertEquals(dtoCoffee.getId(), coffeeId);
        assertEquals(dtoCoffee.getName(), coffeeName);
        assertEquals(dtoCoffee.getOrigin(), coffeeOrigin);
        assertEquals(dtoCoffee.getQuantity(), coffeeQuantity);
        assertEquals(dtoCoffee.getPrice(), coffeePrice);

        assertEquals(dtoClient.getId(), clientId);
        assertEquals(dtoClient.getFirstName(), clientFirstName);
        assertEquals(dtoClient.getLastName(), clientLastName);
        assertEquals(dtoClient.getAge(), clientAge);
        assertEquals(dtoClient.getPhoneNumber(), clientPhoneNumber);

        assertEquals(dtoClientAddr.getId(), addressId);
        assertEquals(dtoClientAddr.getCity(), addressCity);
        assertEquals(dtoClientAddr.getStreet(), addressStreet);
        assertEquals(dtoClientAddr.getNumber(), addressNumber);
    }

    @Test
    public void dtoToOrder() {
        ShopOrder entity = shopOrderMapper.DTOtoShopOrder(orderDto);
        Client entityClient = entity.getClient();
        Coffee entityCoffee = entity.getCoffee();
        Address entityClientAddr = entityClient.getAddress();

        assertEquals(entity.getId(), orderId);
        assertEquals(entity.getStatus(), orderStatus);
        assertEquals(entity.getTime(), time);

        assertEquals(entityCoffee.getId(), coffeeId);
        assertEquals(entityCoffee.getName(), coffeeName);
        assertEquals(entityCoffee.getOrigin(), coffeeOrigin);
        assertEquals(entityCoffee.getQuantity(), coffeeQuantity);
        assertEquals(entityCoffee.getPrice(), coffeePrice);

        assertEquals(entityClient.getId(), clientId);
        assertEquals(entityClient.getFirstName(), clientFirstName);
        assertEquals(entityClient.getLastName(), clientLastName);
        assertEquals(entityClient.getAge(), clientAge);
        assertEquals(entityClient.getPhoneNumber(), clientPhoneNumber);

        assertEquals(entityClientAddr.getId(), addressId);
        assertEquals(entityClientAddr.getCity(), addressCity);
        assertEquals(entityClientAddr.getStreet(), addressStreet);
        assertEquals(entityClientAddr.getNumber(), addressNumber);
    }

}
