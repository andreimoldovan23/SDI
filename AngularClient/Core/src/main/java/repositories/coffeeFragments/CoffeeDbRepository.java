package repositories.coffeeFragments;

import domain.Coffee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import repositories.CoffeeShopRepository;

import java.util.Optional;
import java.util.Set;

public interface CoffeeDbRepository extends CoffeeShopRepository<Coffee, Integer>, CoffeeDbRepositoryCustom {
    @Query("select distinct c from Coffee c")
    @EntityGraph(value = "graph.CoffeeWithOrders", type = EntityGraph.EntityGraphType.LOAD)
    Set<Coffee> findAllWithOrders();

    @Query("select c from Coffee c where c.id = ?1")
    @EntityGraph(value = "graph.CoffeeWithOrders", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Coffee> findByIdWithOrders(Integer id);
}
