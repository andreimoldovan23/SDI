package domain.validator;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.Status;
import domain.Validators.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private OrderValidator orderValidator;
    private ClientValidator clientValidator;
    private CoffeeValidator coffeeValidator;
    private AddressValidator addressValidator;
    private Address address;
    private Client client;
    private Coffee coffee;
    private domain.Order order;

    @BeforeEach
    public void setUp()
    {
        clientValidator = new ClientValidator();
        coffeeValidator = new CoffeeValidator();
        orderValidator = new OrderValidator();
        addressValidator = new AddressValidator();

        client = Client.Builder()
                .id(14)
                .firstName("John")
                .lastName("Mike")
                .addressId(10)
                .age(19)
                .build();
        coffee = Coffee.Builder()
                .id(24)
                .name("Arabica")
                .origin("CentralAmerica")
                .quantity(200)
                .price(50)
                .build();
        order = domain.Order.Builder()
                .id(new ImmutablePair<>(34, 36))
                .status(Status.delivered)
                .time(LocalDateTime.now())
                .build();
        order.getCoffeesId().add(coffee.getId());
        address = Address.Builder()
                .id(14)
                .city("abcd")
                .street("bcde")
                .number(1)
                .build();
    }

    @Test
    public void testClientValidator()
    {
        client.setId(null);
        Exception exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        String expectedMessage = "Id is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setId(15);

        client.setFirstName(null);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "First name is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setFirstName("John");

        client.setLastName(null);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Last name is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setLastName("John");

        client.setAddressId(null);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Address is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setAddressId(10);

        client.setAddressId(-1);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid address id";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setAddressId(10);

        client.setAge(1);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid age";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setAge(19);

        client.setId(0);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid id";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setId(15);

        client.setFirstName("a");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid First Name";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setFirstName("John");

        client.setFirstName("abcd ,?");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "First Name should contain only letters";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setFirstName("John");

        client.setLastName("a");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid Last Name";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setLastName("John");

        client.setLastName("abcd ,?");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Last Name should contain only letters";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setLastName("John");

        client.setPhoneNumber("123");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid phone number";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setPhoneNumber("0123456789");

        client.setPhoneNumber("abcd ,? ####!!!!@@@#!");
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Phone Number should contain only digits";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setPhoneNumber("0123456789");

        client = null;
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Object is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCoffeeValidator()
    {
        coffee.setId(null);
        Exception exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        String expectedMessage = "Id is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setId(25);

        coffee.setName(null);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Name is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setName("something");

        coffee.setOrigin(null);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Origin is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setOrigin("something");

        coffee.setId(0);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Invalid id";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setId(25);

        coffee.setName("abcd ,?");
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Name should contain only letters";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setName("something");

        coffee.setOrigin("abcd ,?");
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Origin should contain only letters";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setOrigin("something");

        coffee.setQuantity(-1);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Quantity cannot be lower than 0";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setQuantity(25);

        coffee.setPrice(-1);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Price cannot be lower than 0";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setPrice(25);

        coffee = null;
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Object is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testOrderValidator()
    {
        order.setId(null);
        Exception exception = assertThrows(OrderValidatorException.class, () -> orderValidator.validate(order));
        String expectedMessage = "Id is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        order.setId(new ImmutablePair<>(34, 36));

        order.setId(new ImmutablePair<>(0, 0));
        exception = assertThrows(OrderValidatorException.class, () -> orderValidator.validate(order));
        expectedMessage = "Invalid id";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        order.setId(new ImmutablePair<>(10, 12));

        order = null;
        exception = assertThrows(OrderValidatorException.class, () -> orderValidator.validate(order));
        expectedMessage = "Object is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddressValidator()
    {
        address.setId(null);
        Exception exception = assertThrows(AddressValidatorException.class, () ->  addressValidator.validate(address));
        String expectedMessage = "Id is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setId(25);

        address.setCity(null);
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "City is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setCity("abcd");

        address.setStreet(null);
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Street is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setStreet("abcd");

        address.setNumber(null);
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Number is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setNumber(1);

        address.setCity("ab");
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Invalid city name";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setCity("abce");

        address.setStreet("");
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Invalid street";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setStreet("abce");

        address.setCity("abccc5464 as");
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Invalid city name";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setCity("abce");

        address.setStreet("abccc5464 as");
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Invalid street name";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setStreet("abce");

        address.setNumber(-1);
        exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        expectedMessage = "Invalid number";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setNumber(1);

        address.setId(-1);
        exception = assertThrows(AddressValidatorException.class, () ->  addressValidator.validate(address));
        expectedMessage = "Invalid id";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        address.setId(25);

        address = null;
        exception = assertThrows(AddressValidatorException.class, () ->  addressValidator.validate(address));
        expectedMessage = "Object is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testVerifyNullableFields() {
        client.setAge(0);
        client.setPhoneNumber("");
        client = clientValidator.verifyNullableFields(client);
        assertNull(client.getAge());
        assertNull(client.getPhoneNumber());

        coffee.setPrice(0);
        coffee.setQuantity(0);
        coffee = coffeeValidator.verifyNullableFields(coffee);
        assertNull(coffee.getPrice());
        assertNull(coffee.getQuantity());
    }

    @Test
    public void testCheckInput() {
        assertEquals(Validator.checkInput("24", "0", true), 24);
        assertNull(Validator.checkInput("0", "0", true));
        assertNull(Validator.checkInput("null", "null", true));

        assertEquals(Validator.checkInput("hello", "", false), "hello");
        assertNull(Validator.checkInput("null", "null", false));
    }

    @AfterEach
    public void tearDown()
    {
        clientValidator = null;
        coffeeValidator = null;
        orderValidator = null;
        addressValidator = null;
        client = null;
        coffee = null;
        order = null;
        address = null;
    }
}
