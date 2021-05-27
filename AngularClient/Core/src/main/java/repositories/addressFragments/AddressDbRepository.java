package repositories.addressFragments;

import domain.Address;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import repositories.CoffeeShopRepository;

import java.util.Set;

public interface AddressDbRepository extends CoffeeShopRepository<Address, Integer> {
    void deleteAddressByNumber(Integer number);

    @Query("select distinct a from Address a")
    @EntityGraph(value = "graph.AddressWithClients", type = EntityGraph.EntityGraphType.LOAD)
    Set<Address> findAllWithClients();

    @Query("select a from Address a where a.id = ?1")
    @EntityGraph(value = "graph.AddressWithClients", type = EntityGraph.EntityGraphType.LOAD)
    Address findByIdWithClients(Integer id);
}
