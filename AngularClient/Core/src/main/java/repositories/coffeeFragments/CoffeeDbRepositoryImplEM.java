package repositories.coffeeFragments;

import domain.Coffee;
import lombok.extern.slf4j.Slf4j;
import repositories.CoffeeShopSupport;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class CoffeeDbRepositoryImplEM extends CoffeeShopSupport implements CoffeeDbRepositoryCustom {
    @Override
    public Set<Coffee> filterCoffeesByName(String name) {
        log.info("Coffee: JPQL - filter by name {}", name);

        EntityManager entityManager = getEntityManager();
        List<Coffee> coffees = entityManager
                .createQuery("select distinct c from Coffee c " +
                        "where c.name like ?1", Coffee.class)
                .setParameter(1, name)
                .getResultList();
        return new HashSet<>(coffees);
    }

    @Override
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        log.info("Coffee: JPQL - filter by origin {}", origin);

        EntityManager entityManager = getEntityManager();
        List<Coffee> coffees = entityManager
                .createQuery("select distinct c from Coffee c " +
                        "where c.origin like ?1", Coffee.class)
                .setParameter(1, origin)
                .getResultList();
        return new HashSet<>(coffees);
    }
}
