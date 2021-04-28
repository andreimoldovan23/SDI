package services.interfaces;

import domain.Coffee;
import domain.ShopOrder;
import domain.Status;
import domain.Validators.ValidatorException;

import java.time.LocalDateTime;
import java.util.Set;

public interface IOrderService extends ICrudService<Integer, ShopOrder> {
    Set<Coffee> filterClientCoffees(Integer client);
    Set<ShopOrder> filterClientOrders(Integer client);
    void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2);
    void buyCoffee(Integer id, Integer client, Integer coffee) throws ValidatorException;
    void changeStatus(Integer ordId, Status status) throws ValidatorException;
}
