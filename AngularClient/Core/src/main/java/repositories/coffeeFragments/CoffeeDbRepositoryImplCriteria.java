package repositories.coffeeFragments;

import domain.Coffee;
import lombok.extern.slf4j.Slf4j;
import repositories.CoffeeShopSupport;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class CoffeeDbRepositoryImplCriteria extends CoffeeShopSupport implements CoffeeDbRepositoryCustom {
    @Override
    public Set<Coffee> filterCoffeesByName(String name) {
        log.info("Coffee: Criteria - filter by name {}", name);

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coffee> query = criteriaBuilder.createQuery(Coffee.class);
        query.distinct(Boolean.TRUE);
        Root<Coffee> root = query.from(Coffee.class);
        query.select(root)
                .where(
                        criteriaBuilder.like(root.get("name").as(String.class), name)
                 );
        List<Coffee> coffees = entityManager.createQuery(query).getResultList();
        return new HashSet<>(coffees);
    }

    @Override
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        log.info("Coffee: Criteria - filter by origin {}", origin);

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coffee> query = criteriaBuilder.createQuery(Coffee.class);
        query.distinct(Boolean.TRUE);
        Root<Coffee> root = query.from(Coffee.class);
        query.select(root)
                .where(
                        criteriaBuilder.like(root.get("origin").as(String.class), origin)
                );
        List<Coffee> coffees = entityManager.createQuery(query).getResultList();
        return new HashSet<>(coffees);
    }
}
