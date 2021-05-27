package repositories;

import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Getter
public abstract class CoffeeShopSupport {
    @PersistenceContext
    private EntityManager entityManager;
}
