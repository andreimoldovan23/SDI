package domain.Validators;

public class ClientValidatorException extends ValidatorException{

    public ClientValidatorException(String message) {
        super("Client: " + message);
    }

}
