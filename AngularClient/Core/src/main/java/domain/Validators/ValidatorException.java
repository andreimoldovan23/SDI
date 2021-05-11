package domain.Validators;

/**
 * Base exception for all the validator exceptions
 */
public class ValidatorException extends BaseException {

    protected String message;

    public ValidatorException(String message) {
        super(message);
        this.message = message;
    }

    public String toString() {
        return message;
    }

}
