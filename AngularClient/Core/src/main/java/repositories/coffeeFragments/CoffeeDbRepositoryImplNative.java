package repositories.coffeeFragments;

import domain.Coffee;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.transaction.annotation.Transactional;
import repositories.CoffeeShopSupport;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class CoffeeDbRepositoryImplNative extends CoffeeShopSupport implements CoffeeDbRepositoryCustom {
    @Transactional
    @Override
    public Set<Coffee> filterCoffeesByName(String name) {
        log.info("Coffee: Native - filter by name {}", name);

        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {c.*} " +
                "from coffee c " +
                "where c.name like ?")
                .addEntity("c", Coffee.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Coffee> coffees = query.setString(1, name).getResultList();
        return new HashSet<>(coffees);
    }

    @Transactional
    @Override
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        log.info("Coffee: Native - filter by origin {}", origin);

        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {c.*} " +
                "from coffee c " +
                "where c.origin like ?")
                .addEntity("c", Coffee.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Coffee> coffees = query.setString(1, origin).getResultList();
        return new HashSet<>(coffees);
    }
}
