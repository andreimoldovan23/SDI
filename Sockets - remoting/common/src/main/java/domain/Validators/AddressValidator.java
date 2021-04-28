package domain.Validators;

import domain.Address;

import java.util.Optional;

public class AddressValidator implements Validator<Integer, Address>{

    @Override
    public void validate(Address address) throws AddressValidatorException
    {
        checkNull(address, "Object is null");
        checkNull(address.getId(), "Id is null");
        checkNull(address.getCity(), "City is null");
        checkNull(address.getStreet(), "Street is null");
        checkNull(address.getNumber(), "Number is null");

        validateStringLength(address.getCity(), 3, "Invalid city name");
        validateStringLength(address.getStreet(), 3, "Invalid street");
        validateString(address.getCity(), "Invalid city name");
        validateString(address.getStreet(), "Invalid street name");
        validateNumber(address.getNumber(), "Invalid number");
        checkId(address.getId(), "Invalid id");
    }

    @Override
    public void checkNull(Object obj, String message) throws AddressValidatorException {
        Optional.ofNullable(obj).orElseThrow(() -> {
            throw new AddressValidatorException(message);
        });
    }

    @Override
    public void checkId(Integer id, String message) throws AddressValidatorException {
        Optional.of(id).filter(e -> e > 0).orElseThrow(() -> {
            throw new AddressValidatorException(message);
        });
    }

    @Override
    public Address verifyNullableFields(Address address) {
        return address;
    }

    private void validateStringLength(String string, Integer length, String message) throws AddressValidatorException
    {
        Optional.ofNullable(string).ifPresent(val -> Optional.of(val).filter(s -> s.length() > length)
                .orElseThrow(() -> {
                    throw new AddressValidatorException(message);
                }));
    }

    private void validateString(String str, String message) throws AddressValidatorException
    {
        Optional.ofNullable(str).filter(e -> e.matches("^[a-zA-Z\\s]*$+")).orElseThrow(() ->
        {
            throw new AddressValidatorException(message);
        });
    }

    private void validateNumber(Integer nb, String message) throws AddressValidatorException
    {
        Optional.of(nb).filter(e -> e > 0).orElseThrow(() -> {
            throw new AddressValidatorException(message);
        });
    }
}
