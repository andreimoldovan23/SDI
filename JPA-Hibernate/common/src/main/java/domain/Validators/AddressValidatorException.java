package domain.Validators;

public class AddressValidatorException extends ValidatorException{

    public AddressValidatorException(String message) {
        super("Address: " + message);
    }

}