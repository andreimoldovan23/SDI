package repositories;

import domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDbRepository extends JpaRepository<Address, Integer> {
    void deleteAddressByNumber(Integer number);
}
