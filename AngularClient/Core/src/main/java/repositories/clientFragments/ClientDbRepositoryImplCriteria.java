package repositories.clientFragments;

import domain.Client;
import lombok.extern.slf4j.Slf4j;
import repositories.CoffeeShopSupport;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("DuplicatedCode")
@Slf4j
public class ClientDbRepositoryImplCriteria extends CoffeeShopSupport implements ClientDbRepositoryCustom {
    @Override
    public Set<Client> filterClientByFirstNameOrLastName(String name) {
        log.info("Client: Criteria - filter by firstName/lastName {}", name);

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
        query.distinct(Boolean.TRUE);
        Root<Client> root = query.from(Client.class);
        root.fetch("address", JoinType.INNER);
        query.select(root)
                .where(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName").as(String.class), name),
                        criteriaBuilder.like(root.get("lastName").as(String.class), name)
                ));
        List<Client> clients = entityManager.createQuery(query).getResultList();
        return new HashSet<>(clients);
    }

    @Override
    public Set<Client> filterClientByAgeInterval(Integer age1, Integer age2) {
        log.info("Client: Criteria - filter by age interval {} - {}", age1, age2);

        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = criteriaBuilder.createQuery(Client.class);
        query.distinct(Boolean.TRUE);
        Root<Client> root = query.from(Client.class);
        root.fetch("address", JoinType.INNER);
        query.select(root)
                .where(criteriaBuilder.and(
                        criteriaBuilder.ge(root.get("clientInfo").get("age").as(Integer.class), age1),
                        criteriaBuilder.le(root.get("clientInfo").get("age").as(Integer.class), age2)
                ));
        List<Client> clients = entityManager.createQuery(query).getResultList();
        return new HashSet<>(clients);
    }
}
