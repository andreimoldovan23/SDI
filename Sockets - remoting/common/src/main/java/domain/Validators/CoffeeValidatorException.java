package domain.Validators;

public class CoffeeValidatorException extends ValidatorException{

    public CoffeeValidatorException(String message) {
        super("Coffee: " + message);
    }

}
