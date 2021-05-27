package repositories.clientFragments;

import domain.Address;
import domain.Client;
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
public class ClientDbRepositoryImplNative extends CoffeeShopSupport implements ClientDbRepositoryCustom {
    @Transactional
    @Override
    public Set<Client> filterClientByFirstNameOrLastName(String name) {
        log.info("Client: Native - filter by firstName/lastName {}", name);

        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {c.*},{a.*} " +
                "from client c " +
                "inner join address a on a.id=c.address_id " +
                "where c.firstName like ? or c.lastName like ?")
                .addEntity("c", Client.class)
                .addJoin("a", "c.address")
                .addEntity("c", Client.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Client> clients = query.setString(1, name).setString(2, name).getResultList();
        return new HashSet<>(clients);
    }

    @Transactional
    @Override
    public Set<Client> filterClientByAgeInterval(Integer age1, Integer age2) {
        log.info("Client: Native - filter by age interval {} - {}", age1, age2);

        HibernateEntityManager hibernateEntityManager = getEntityManager().unwrap(HibernateEntityManager.class);
        Session session = hibernateEntityManager.getSession();

        org.hibernate.Query query = session.createSQLQuery("select distinct {c.*},{a.*} " +
                "from client c " +
                "inner join address a on a.id=c.address_id " +
                "where c.age >= ? and c.age <= ?")
                .addEntity("c", Client.class)
                .addJoin("a", "c.address")
                .addEntity("c", Client.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Client> clients = query.setInteger(1, age1).setInteger(2, age2).getResultList();
        return new HashSet<>(clients);
    }
}
