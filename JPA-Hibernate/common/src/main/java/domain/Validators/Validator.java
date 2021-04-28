package domain.Validators;

import domain.BaseEntity;

/**
 * Interface for generic validation operations.
 * @param <T> Entity to be validated.
 */

public interface Validator<ID, T extends BaseEntity<ID>> {
    /**
     *
     * @param entity The entity that is checked.
     * @throws ValidatorException if there is an invalid property.
     */
    void validate(T entity) throws ValidatorException;

    /**
     *
     * @param obj The object that will be checked.
     * @param errorMessage The the message of the exception.
     * @throws ValidatorException if the object is null.
     */
    void checkNull(Object obj, String errorMessage) throws ValidatorException;

    /**
     *
     * @param id The id that will be checked.
     * @param message The the message of the exception.
     * @throws ValidatorException if the id is invalid.
     */
    void checkId(ID id, String message) throws ValidatorException;

    /**
     * Helper function that checks the equality between an input string and another one
     * and returns the input if they are not equal or converts it into a null otherwise
     * @param input: the input string to be verified
     * @return String: null if the string was empty or the string itself otherwise
     */
    static Object checkInput(String input, String checkAgainst, Boolean shouldBeNumber) throws NumberFormatException {
        return input.equals(checkAgainst) ? null : (shouldBeNumber ? Integer.parseInt(input) : input);
    }

}

