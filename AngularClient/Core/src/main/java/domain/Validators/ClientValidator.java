package domain.Validators;

import domain.Client;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
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
        checkNull(client.getFirstName(), "First name is null");
        checkNull(client.getLastName(), "Last name is null");
        checkNull(client.getAddress(), "Address is null");

        validateStringLength(client.getFirstName(), 1, "Invalid First Name");
        validateStringLength(client.getLastName(), 1, "Invalid Last Name");

        validateStringPattern(client.getFirstName(), "([a-zA-Z])+([ -'][a-zA-Z]{1,})*", "Invalid first name");
        validateStringPattern(client.getLastName(), "([a-zA-Z])+([ -'][a-zA-Z]{1,})*", "Invalid last name");

        validateAge(client.getAge(), "Invalid age");
        validateStringLength(client.getPhoneNumber(), 9, "Invalid phone number");
        validateStringPattern(client.getPhoneNumber(), "^(\\+[1-9]{1,2})?0[1-9][0-9]{8,}$", "Invalid phone number");
    }

    @Override
    public void checkNull(Object obj, String message) throws ClientValidatorException {
        Optional.ofNullable(obj).orElseThrow(() -> {
            throw new ClientValidatorException(message);
        });
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
        Optional.ofNullable(string).ifPresent(val -> Optional.of(val).filter(s -> s.length() > length)
                .orElseThrow(() -> {
                    throw new ClientValidatorException(message);
                }));
    }

}
