package mappers;

import server_config.WebConfig;
import domain.*;
import dtos.AddressDTO;
import dtos.ClientDTO;
import dtos.CoffeeDTO;
import dtos.ShopOrderDTO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@ContextConfiguration(classes = WebConfig.class)
public class AbstractMapperTest {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd//HH::mm::ss");

    protected static final Integer addressId = 10;
    protected static final String addressCity = "LosAngeles";
    protected static final String addressStreet = "PalmStreet";
    protected static final Integer addressNumber = 45;

    protected static final Integer clientId = 10;
    protected static final String clientFirstName = "John";
    protected static final String clientLastName = "Doe";
    protected static final Integer clientAge = 45;
    protected static final String clientPhoneNumber = "0000000000";

    protected static final Integer coffeeId = 10;
    protected static final String coffeeName = "Arabica";
    protected static final String coffeeOrigin = "Brazil";
    protected static final Integer coffeeQuantity = 200;
    protected static final Integer coffeePrice = 200;

    protected static final Integer orderId = 10;
    protected static final Status orderStatus = Status.pending;
    protected static final LocalDateTime time = LocalDateTime.of(2000, 2, 2, 2, 2, 2);
    protected static final String timeString = time.format(formatter);

    protected static final Address address = Address.builder()
            .city(addressCity)
            .street(addressStreet)
            .number(addressNumber)
            .build();

    protected static final Client client = Client.builder()
            .firstName(clientFirstName)
            .lastName(clientLastName)
            .address(address)
            .build();

    protected static final Coffee coffee = Coffee.builder()
            .name(coffeeName)
            .origin(coffeeOrigin)
            .quantity(coffeeQuantity)
            .price(coffeePrice)
            .build();

    protected static final ShopOrder order = ShopOrder.builder()
            .status(orderStatus)
            .time(time)
            .client(client)
            .coffee(coffee)
            .build();

    protected static final AddressDTO addressDto = new AddressDTO(addressId, addressCity, addressStreet, addressNumber);
    protected static final ClientDTO clientDto = new ClientDTO(clientId, clientFirstName, clientLastName, clientAge,
            clientPhoneNumber, addressDto);
    protected static final CoffeeDTO coffeeDto = new CoffeeDTO(coffeeId, coffeeName, coffeeOrigin, coffeeQuantity,
            coffeePrice);
    protected static final ShopOrderDTO orderDto = new ShopOrderDTO(orderId, orderStatus, timeString,
            clientDto, coffeeDto);

}
