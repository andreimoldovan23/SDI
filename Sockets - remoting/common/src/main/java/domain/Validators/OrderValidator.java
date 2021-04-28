package domain.Validators;

import domain.Order;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class OrderValidator implements Validator<Pair<Integer, Integer>, Order> {
    /**
     * Constructor for OrderValidator. Performs different checks
     * @param entity: the order to be verified
     * @throws OrderValidatorException
     *          in case of null objects/fields or invalid data
     */
    @Override
    public void validate(Order entity) throws OrderValidatorException {
        checkNull(entity, "Object is null");
        checkNull(entity.getId(), "Id is null");
        Optional.of(entity.getCoffeesId().size())
                .filter(s -> s == 0)
                .ifPresent(s -> {
                    throw new OrderValidatorException("No coffees in the order");
                });

        checkId(entity.getId(), "Invalid id");
    }

    @Override
    public void checkNull(Object obj, String message) throws OrderValidatorException {
        Optional.ofNullable(obj).orElseThrow(() -> {
            throw new OrderValidatorException(message);
        });
    }

    @Override
    public void checkId(Pair<Integer, Integer> id, String message) throws OrderValidatorException {
        Optional.of(id.getLeft()).filter(val -> val > 0).orElseThrow(() -> {
            throw new OrderValidatorException(message);
        });
        Optional.of(id.getRight()).filter(val -> val > 0).orElseThrow(() -> {
            throw new OrderValidatorException(message);
        });
    }

    @Override
    public Order verifyNullableFields(Order entity) {
        return entity;
    }

}
