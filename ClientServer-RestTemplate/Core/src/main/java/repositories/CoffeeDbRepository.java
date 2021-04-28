package repositories;

import domain.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CoffeeDbRepository extends JpaRepository<Coffee, Integer> {
    @Query("select c from Coffee c where c.name like %:name%")
    Set<Coffee> filterCoffeesByName(@Param("name") String name);

    @Query("select c from Coffee c where c.origin like %:origin%")
    Set<Coffee> filterCoffeesByOrigin(@Param("origin") String origin);
}
