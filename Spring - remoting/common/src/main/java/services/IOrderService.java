package services;

import domain.Coffee;
import domain.Order;
import domain.Status;
import domain.Validators.ValidatorException;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IOrderService extends ICrudService<Pair<Integer, Integer>, Order> {
    Set<Coffee> filterClientCoffees(Integer clientId);
    Set<Order> filterClientOrders(Integer clientId);
    void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2);
    void buyCoffee(Integer id, Integer clientId, Integer coffeeId) throws ValidatorException;
    void changeStatus(Integer ordId, Integer clientId, Status status) throws ValidatorException;
    void deleteCoffeesFromOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) throws ValidatorException;
    void addCoffeesToOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) throws ValidatorException;
}
