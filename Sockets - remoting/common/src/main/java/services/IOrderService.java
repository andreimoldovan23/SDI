package services;

import domain.Coffee;
import domain.Order;
import domain.Status;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IOrderService extends ICrudService<Pair<Integer, Integer>, Order> {
    CompletableFuture<Set<Coffee>> filterClientCoffees(Integer clientId);
    CompletableFuture<Set<Order>> filterClientOrders(Integer clientId);
    void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2);
    void buyCoffee(Integer id, Integer clientId, Integer coffeeId);
    void changeStatus(Integer ordId, Integer clientId, Status status);
    void deleteCoffeesFromOrder(Integer ordId, Integer clientId, List<Integer> coffeesId);
    void addCoffeesToOrder(Integer ordId, Integer clientId, List<Integer> coffeesId);
}
