package repositories;

import domain.Coffee;
import domain.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface OrderDbRepository extends JpaRepository<ShopOrder, Integer> {

    @Query("select o.coffee from ShopOrder o where o.client.id = ?1")
    Set<Coffee> filterClientCoffees(Integer clientId);

    @Query("select o from ShopOrder o where o.client.id = ?1")
    Set<ShopOrder> filterClientOrders(Integer clientId);

}
