package domain.Validators;

/**
 * Base exception
 */
public abstract class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

}
