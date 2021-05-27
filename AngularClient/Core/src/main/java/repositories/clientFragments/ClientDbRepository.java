package repositories.clientFragments;

import domain.Client;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import repositories.CoffeeShopRepository;

import java.util.Optional;
import java.util.Set;

public interface ClientDbRepository extends CoffeeShopRepository<Client, Integer>, ClientDbRepositoryCustom {
    @Query("select count(c) from Client c where c.address.id = ?1")
    Integer countClientsLivingAtAddress(Integer id);

    @Query("select distinct c from Client c")
    @EntityGraph(value = "graph.ClientWithAddress", type = EntityGraph.EntityGraphType.LOAD)
    Set<Client> findAllWithAddresses();

    @Query("select c from Client c where c.id = ?1")
    @EntityGraph(value = "graph.ClientWithAddress", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Client> findByIdWithAddress(Integer id);

    @Query("select c from Client c where c.id = ?1")
    @EntityGraph(value = "graph.ClientWithOrders", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Client> findByIdWithOrders(Integer id);

    @Query("select distinct c from Client c")
    @EntityGraph(value = "graph.ClientWithOrders", type = EntityGraph.EntityGraphType.LOAD)
    Set<Client> findAllWithOrders();
}
