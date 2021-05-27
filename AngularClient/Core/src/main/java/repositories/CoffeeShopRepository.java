package repositories;

import domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
@Transactional
public interface CoffeeShopRepository<Entity extends BaseEntity<ID>, ID extends Integer>
        extends JpaRepository<Entity, ID> {
}
