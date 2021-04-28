package services.implementations;

import domain.Coffee;
import domain.Validators.CoffeeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.CoffeeDbRepository;
import services.interfaces.ICoffeeService;

import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
public class CoffeeService extends Service<Integer, Coffee> implements ICoffeeService {

    /**
     * Constructor for CoffeeService
     * @param constructorRepo of type {@code Repository<Integer, Coffee>}
     */
    public CoffeeService(CoffeeDbRepository constructorRepo, CoffeeValidator coffeeValidator) {
        super(constructorRepo, coffeeValidator);
    }

    /**
     * Gets the coffees that have a substring in their name in a set
     * @param name of type String
     * @return {@code Set<Coffee>}
     */
    @Transactional
    public Set<Coffee> filterCoffeesByName(String name) {
        log.info("filterCoffeesByName - method entered - name={}", name);
        Set<Coffee> result = ((CoffeeDbRepository)this.repository).filterCoffeesByName(name);
        log.info("filterCoffeesByName result={} - method finished", result);
        return result;
    }

    /**
     * Gets the coffees that have a substring in their origin in a set
     * @param origin : string
     * @return {@code Set<Coffee>}
     */
    @Transactional
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        log.info("filterCoffeesByOrigin - method entered - origin={}", origin);
        Set<Coffee> result = ((CoffeeDbRepository)this.repository).filterCoffeesByOrigin(origin);
        log.info("filterCoffeesByOrigin result={} - method finished", result);
        return result;
    }

}
