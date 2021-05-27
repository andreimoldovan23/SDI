package validators;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.Validators.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ValidatorTest {

    private ClientValidator clientValidator;
    private CoffeeValidator coffeeValidator;
    private AddressValidator addressValidator;
    private Address address;
    private Client client;
    private Coffee coffee;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
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
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("First name is null");
        clientValidator.validate(client);
        client.setFirstName("John");

        client.setLastName(null);
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Last name is null");
        clientValidator.validate(client);
        client.setLastName("John");

        client.setAge(1);
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid age");
        clientValidator.validate(client);
        client.setAge(19);

        client.setFirstName("a");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid first name");
        clientValidator.validate(client);
        client.setFirstName("John");

        client.setFirstName("abcd ,?");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid first name");
        clientValidator.validate(client);
        client.setFirstName("John");

        client.setLastName("a");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid last name");
        clientValidator.validate(client);
        client.setLastName("John");

        client.setLastName("abcd ,?");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid last name");
        clientValidator.validate(client);
        client.setLastName("John");

        client.setPhoneNumber("123");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid phone number");
        clientValidator.validate(client);
        client.setPhoneNumber("0123456789");

        client.setPhoneNumber("abcd ,? ####!!!!@@@#!");
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Invalid phone number");
        clientValidator.validate(client);
        client.setPhoneNumber("0123456789");

        client = null;
        exceptionRule.expect(ClientValidatorException.class);
        exceptionRule.expectMessage("Object is null");
        clientValidator.validate(client);
    }

    @Test
    public void testCoffeeValidator()
    {
        coffee.setName(null);
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Name is null");
        coffeeValidator.validate(coffee);
        coffee.setName("something");

        coffee.setOrigin(null);
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Origin is null");
        coffeeValidator.validate(coffee);
        coffee.setOrigin("something");

        coffee.setName("abcd ,?");
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Invalid name");
        coffeeValidator.validate(coffee);
        coffee.setName("something");

        coffee.setOrigin("abcd ,?");
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Invalid origin");
        coffeeValidator.validate(coffee);
        coffee.setOrigin("something");

        coffee.setQuantity(-1);
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Quantity cannot be lower than 0");
        coffeeValidator.validate(coffee);
        coffee.setQuantity(25);

        coffee.setPrice(-1);
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Price cannot be lower than 0");
        coffeeValidator.validate(coffee);
        coffee.setPrice(25);

        coffee = null;
        exceptionRule.expect(CoffeeValidatorException.class);
        exceptionRule.expectMessage("Object is null");
        coffeeValidator.validate(coffee);
    }

    @Test
    public void testAddressValidator()
    {
        address.setCity(null);
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("City is null");
        addressValidator.validate(address);
        address.setCity("abcd");

        address.setStreet(null);
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Street is null");
        addressValidator.validate(address);
        address.setStreet("abcd");

        address.setNumber(null);
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Number is null");
        addressValidator.validate(address);
        address.setNumber(1);

        address.setCity("a");
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Invalid city name");
        addressValidator.validate(address);
        address.setCity("abce");

        address.setStreet("");
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Invalid street");
        addressValidator.validate(address);
        address.setStreet("abce");

        address.setCity("abccc5464 as");
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Invalid city name");
        addressValidator.validate(address);
        address.setCity("abce");

        address.setStreet("abccc5464 as");
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Invalid street name");
        addressValidator.validate(address);
        address.setStreet("abce");

        address.setNumber(-1);
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Invalid number");
        addressValidator.validate(address);
        address.setNumber(1);

        address = null;
        exceptionRule.expect(AddressValidatorException.class);
        exceptionRule.expectMessage("Object is null");
        addressValidator.validate(address);
    }

    @Test
    public void testCheckInput() {
        assertEquals(Validator.checkInput("24", "0", true), 24);
        assertNull(Validator.checkInput("0", "0", true));
        assertNull(Validator.checkInput("null", "null", true));

        assertEquals(Validator.checkInput("hello", "", false), "hello");
        assertNull(Validator.checkInput("null", "null", false));
    }

    @After
    public void tearDown() {
        clientValidator = null;
        coffeeValidator = null;
        addressValidator = null;
        client = null;
        coffee = null;
        address = null;
    }

}
