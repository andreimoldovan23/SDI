package domain.Validators;

import domain.Coffee;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class CoffeeValidator implements Validator<Integer, Coffee> {

    /**
     * Constructor for CoffeeValidator. Performs different checks
     * @param entity: the client to be verified
     * @throws CoffeeValidatorException
     *          in case of null objects/fields or invalid data
     */
    @Override
    public void validate(Coffee entity) throws CoffeeValidatorException {
        checkNull(entity, "Object is null");
        checkNull(entity.getName(), "Name is null");
        checkNull(entity.getOrigin(), "Origin is null");

        checkNumberField(entity.getQuantity(), "Quantity cannot be lower than 0");
        checkNumberField(entity.getPrice(), "Price cannot be lower than 0");

        validateStringPattern(entity.getName(), "([a-zA-Z])+([ -'][a-zA-Z]{1,})*", "Name should contain only letters");
        validateStringPattern(entity.getOrigin(), "([a-zA-Z])+([ -'][a-zA-Z]{1,})*", "Origin should contain only letters");
    }

    @Override
    public void checkNull(Object obj, String message) throws CoffeeValidatorException {
        Optional.ofNullable(obj).orElseThrow(() -> {
            throw new CoffeeValidatorException(message);
        });
    }

    /**
     *
     * @param string the string that needs to be checked
     * @param characters the pattern used in checking the string
     * @param message the exception message
     * @throws CoffeeValidatorException if the string does not match the pattern
     */
    private void validateStringPattern(String string, String characters, String message) throws ClientValidatorException {
        Optional.ofNullable(string).ifPresent(val -> Optional.of(val).filter(s -> Pattern.matches(characters, s))
                .orElseThrow( () -> {
                    throw new CoffeeValidatorException(message);
                }));
    }

    /**
     *
     * @param number The number that will be checked.
     * @param message The exception message.
     * @throws CoffeeValidatorException If the number is negative.
     */
    private void checkNumberField(Integer number, String message) throws CoffeeValidatorException {
        Optional.ofNullable(number).ifPresent(value -> Optional.of(value).filter(n -> n >= 0).orElseThrow(() -> {
            throw new CoffeeValidatorException(message);
        }));
    }

}
