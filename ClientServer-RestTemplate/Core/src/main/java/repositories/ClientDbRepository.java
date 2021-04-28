package repositories;

import domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ClientDbRepository extends JpaRepository<Client, Integer> {
    @Query("select c from Client c where c.firstName like %:name% or c.lastName like %:name%")
    Set<Client> filterClientByFirstNameOrLastName(@Param("name") String name);

    @Query("select c from Client c where c.age >= ?1 and c.age <= ?2")
    Set<Client> filterClientByAgeInterval(Integer age1, Integer age2);
}
