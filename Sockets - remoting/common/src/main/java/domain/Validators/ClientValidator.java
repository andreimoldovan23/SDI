package domain.Validators;

import domain.Client;

import java.util.Optional;
import java.util.regex.Pattern;

public class ClientValidator implements Validator<Integer, Client> {

    /**
     * Constructor for ClientValidator. Performs different checks
     * @param client: the client to be verified
     * @throws ClientValidatorException
     *          in case of null objects/fields or invalid data
     */
    @Override
    public void validate(Client client) throws ClientValidatorException
    {
        checkNull(client, "Object is null");
        checkNull(client.getId(), "Id is null");
        checkNull(client.getFirstName(), "First name is null");
        checkNull(client.getLastName(), "Last name is null");
        checkNull(client.getAddressId(), "Address is null");
        validateAge(client.getAge(), "Invalid age");
        checkId(client.getId(), "Invalid id");
        checkId(client.getAddressId(), "Invalid address id");

        validateStringLength(client.getFirstName(), 3, "Invalid First Name");
        validateStringLength(client.getLastName(), 3, "Invalid Last Name");
        validateStringLength(client.getPhoneNumber(), 10, "Invalid phone number");

        validateStringPattern(client.getFirstName(), "[a-zA-Z]+", "First Name should contain only letters");
        validateStringPattern(client.getLastName(), "[a-zA-Z]+", "Last Name should contain only letters");
        validateStringPattern(client.getPhoneNumber(), "[0-9]+", "Phone Number should contain only digits");
    }

    @Override
    public void checkNull(Object obj, String message) throws ClientValidatorException {
        Optional.ofNullable(obj).orElseThrow(() -> {
            throw new ClientValidatorException(message);
        });
    }

    @Override
    public void checkId(Integer id, String message) throws ClientValidatorException {
        Optional.of(id).filter(e -> e > 0).orElseThrow(() -> {
            throw new ClientValidatorException(message);
        });
    }

    /**
     * Overrides its parent method. Checks age and phone number to see if after reading from the xml they should be null or not
     * @param entity: the client entity to be checked and updated
     * @return Client: the checked and updated client entity
     */
    @Override
    public Client verifyNullableFields(Client entity) {
        Integer age = entity.getAge();
        String phoneNumber = entity.getPhoneNumber();
        entity.setAge(age == 0 ? null : age);
        entity.setPhoneNumber(phoneNumber.equals("") ? null : phoneNumber);
        return entity;
    }

    /**
     *
     * @param age The age of the client that will be checked.
     * @param message The exception message.
     * @throws ClientValidatorException If the client age is not between 18 and 98
     */
    private void validateAge(Integer age, String message) throws ClientValidatorException
    {
        Optional.ofNullable(age).ifPresent(val -> Optional.of(val).filter(e -> 18 <= e && e <= 98)
                    .orElseThrow(() -> {
                        throw new ClientValidatorException(message);
                    }));
    }

    /**
     *
     * @param string the string that needs to be checked
     * @param characters the pattern used in checking the string
     * @param message the exception message
     * @throws ClientValidatorException if the string does not match the pattern
     */
    private void validateStringPattern(String string, String characters, String message) throws ClientValidatorException {
        Optional.ofNullable(string).ifPresent(val -> Optional.of(val).filter(s -> Pattern.matches(characters, s))
        .orElseThrow( () -> {
            throw new ClientValidatorException(message);
        }));
    }

    /**
     *
     * @param string The string that will be checked.
     * @param length The minimal length of the string
     * @param message Exception message.
     * @throws ClientValidatorException if the length of the string is smaller than the given length
     */
    private void validateStringLength(String string, Integer length, String message) throws ClientValidatorException
    {
        Optional.ofNullable(string).ifPresent(val -> Optional.of(val).filter(s -> s.length() >= length)
                .orElseThrow(() -> {
                    throw new ClientValidatorException(message);
                }));
    }

}
