package repositories.clientFragments;

import domain.Client;
import lombok.extern.slf4j.Slf4j;
import repositories.CoffeeShopSupport;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ClientDbRepositoryImplEM extends CoffeeShopSupport implements ClientDbRepositoryCustom {
    @Override
    public Set<Client> filterClientByFirstNameOrLastName(String name) {
        log.info("Client: JPQL - filter by firstName/lastName {}", name);

        EntityManager entityManager = getEntityManager();
        List<Client> clients = entityManager
                .createQuery("select distinct c from Client c inner join fetch c.address " +
                        "where c.firstName like ?1 or c.lastName like ?1", Client.class)
                .setParameter(1, name)
                .getResultList();
        return new HashSet<>(clients);
    }

    @Override
    public Set<Client> filterClientByAgeInterval(Integer age1, Integer age2) {
        log.info("Client: JPQL - filter by age interval {} - {}", age1, age2);

        EntityManager entityManager = getEntityManager();
        List<Client> clients = entityManager
                .createQuery("select distinct c from Client c inner join fetch c.address " +
                        "where c.clientInfo.age >= ?1 and c.clientInfo.age <= ?2", Client.class)
                .setParameter(1, age1)
                .setParameter(2, age2)
                .getResultList();
        return new HashSet<>(clients);
    }
}
