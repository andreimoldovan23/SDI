package domain.Validators;

public class DatabaseException extends ValidatorException{
    public DatabaseException(String message) {
        super("Database: " + message);
    }
}
