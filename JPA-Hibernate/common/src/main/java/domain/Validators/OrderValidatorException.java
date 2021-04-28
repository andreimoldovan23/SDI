package domain.Validators;

public class OrderValidatorException extends ValidatorException{
    public OrderValidatorException(String message) {
        super("ShopOrder: " + message);
    }
}
