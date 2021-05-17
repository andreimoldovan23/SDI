package validators;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.Validators.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private ClientValidator clientValidator;
    private CoffeeValidator coffeeValidator;
    private AddressValidator addressValidator;
    private Address address;
    private Client client;
    private Coffee coffee;

    @BeforeEach
    public void setUp()
    {
        clientValidator = new ClientValidator();
        coffeeValidator = new CoffeeValidator();
        addressValidator = new AddressValidator();

        client = Client.builder()
                .firstName("John")
                .lastName("Mike")
                .build();
        coffee = Coffee.builder()
                .name("Arabica")
                .origin("Central America")
                .quantity(200)
                .price(50)
                .build();
        address = Address.builder()
                .city("Los Angeles")
                .street("Wolfgang Goethe")
                .number(1)
                .build();
        client.setAddress(address);
    }

    @Test
    public void testClientValidator()
    {
        client.setFirstName(null);
        Exception exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        String expectedMessage = "First name is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setFirstName("John");

        client.setLastName(null);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Last name is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setLastName("John");

        client.setAge(1);
        exception = assertThrows(ClientValidatorException.class, () -> clientValidator.validate(client));
        expectedMessage = "Invalid age";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        client.setAge(19);

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
        coffee.setName(null);
        Exception exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        String expectedMessage = "Name is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setName("something");

        coffee.setOrigin(null);
        exception = assertThrows(CoffeeValidatorException.class, () -> coffeeValidator.validate(coffee));
        expectedMessage = "Origin is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        coffee.setOrigin("something");

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
    public void testAddressValidator()
    {
        address.setCity(null);
        Exception exception = assertThrows(AddressValidatorException.class, () -> addressValidator.validate(address));
        String expectedMessage = "City is null";
        String actualMessage = exception.getMessage();
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

        address.setCity("a");
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

        address = null;
        exception = assertThrows(AddressValidatorException.class, () ->  addressValidator.validate(address));
        expectedMessage = "Object is null";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
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
    public void tearDown() {
        clientValidator = null;
        coffeeValidator = null;
        addressValidator = null;
        client = null;
        coffee = null;
        address = null;
    }

}
