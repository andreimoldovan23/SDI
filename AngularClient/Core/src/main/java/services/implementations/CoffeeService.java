package services.implementations;

import domain.Coffee;
import domain.Validators.CoffeeValidator;
import domain.Validators.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.CoffeeDbRepository;
import repositories.OrderDbRepository;
import services.interfaces.ICoffeeService;

import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
public class CoffeeService extends Service<Integer, Coffee> implements ICoffeeService {

    private final OrderDbRepository orderDbRepository;

    /**
     * Constructor for CoffeeService
     * @param constructorRepo of type {@code Repository<Integer, Coffee>}
     */
    public CoffeeService(CoffeeDbRepository constructorRepo, CoffeeValidator coffeeValidator,
                         OrderDbRepository orderDbRepository) {
        super(constructorRepo, coffeeValidator);
        this.orderDbRepository = orderDbRepository;
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

    /**
     * Gets the number of clients that ordered that coffee
     * @param id : the id of the coffee
     * @return Integer : the number of clients
     */
    @Transactional
    public Integer byHowManyClientsWasOrdered(Integer id) {
        log.info("byHowManyClientsWasOrdered - method entered - id={}", id);
        Coffee coffee = this.findOne(id);
        return this.orderDbRepository.byHowManyClientsWasItOrdered(coffee.getId());
    }

    /**
     * Updates an element
     * @param element of type coffee
     */
    @Transactional
    public void Update(Coffee element) throws ValidatorException {
        log.info("update coffee - method entered");
        validator.validate(element);
        repository.findById(element.getId()).ifPresentOrElse(elem ->
        {
            elem.setPrice(element.getPrice());
            elem.setQuantity(element.getQuantity());
            elem.setName(element.getName());
            elem.setOrigin(element.getOrigin());
            repository.save(elem);
        }, () -> {
            log.info("update coffee - throwing exception");
            throw new ValidatorException("No such element");
        });
        log.info("update coffee - method finished");
    }

}
