package services.implementations;

import domain.Coffee;
import domain.ShopOrder;
import domain.Validators.CoffeeValidator;
import domain.Validators.CoffeeValidatorException;
import domain.Validators.ValidatorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import repositories.coffeeFragments.CoffeeDbRepository;
import services.interfaces.ICoffeeService;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CoffeeService implements ICoffeeService {

    private final CoffeeDbRepository coffeeDbRepository;
    private final CoffeeValidator coffeeValidator;

    /**
     * Gets a set of the coffees that have a certain substring in their name
     * @param name: String
     * @return {@code Set<Coffee>}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<Coffee> filterCoffeesByName(String name) {
        log.info("filterCoffeesByName - method entered - name={}", name);
        Set<Coffee> result = coffeeDbRepository.filterCoffeesByName(name);
        log.info("filterCoffeesByName result={} - method finished", result);
        return result;
    }

    /**
     * Gets a set of the coffees that have a certain substring in their origin
     * @param origin : String
     * @return {@code Set<Coffee>}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        log.info("filterCoffeesByOrigin - method entered - origin={}", origin);
        Set<Coffee> result = coffeeDbRepository.filterCoffeesByOrigin(origin);
        log.info("filterCoffeesByOrigin result={} - method finished", result);
        return result;
    }

    /**
     * Gets the number of clients that ordered that coffee
     * @param id : Integer, the id of the coffee
     * @return Integer : the number of clients
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Integer byHowManyClientsWasOrdered(Integer id) {
        log.info("byHowManyClientsWasOrdered - method entered - id={}", id);
        return findOneWithOrders(id).getOrders().stream()
                .map(ShopOrder::getClient)
                .collect(Collectors.toSet())
                .size();
    }

    /**
     * Adds a coffee
     * @param entity: Coffee (to be added)
     * @throws ValidatorException
     *      if the coffee is not valid or if it already exists
     */
    @Transactional
    @Override
    public void Add(Coffee entity) throws ValidatorException {
        log.info("add coffee - method entered");
        coffeeValidator.validate(entity);
        try {
            findOneWithOrders(entity.getId());
            throw new ValidatorException("Entity already exists");
        } catch (CoffeeValidatorException ce) {
            coffeeDbRepository.save(entity);
        }
    }

    /**
     * Updates a coffee
     * @param element: Coffee (to be updated)
     * @throws ValidatorException
     *          if the coffee is not valid or if it doesn't exist
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void Update(Coffee element) throws ValidatorException {
        log.info("update coffee - method entered");

        coffeeValidator.validate(element);
        Coffee coffee = findOneWithOrders(element.getId());
        coffee.setPrice(element.getPrice());
        coffee.setQuantity(element.getQuantity());
        coffee.setName(element.getName());
        coffee.setOrigin(element.getOrigin());
        coffeeDbRepository.save(coffee);

        log.info("update coffee - method finished");
    }

    /**
     * Deletes a coffee by id
     * @param id: Integer, id of coffee
     * @throws ValidatorException
     *          if the coffee does not exist
     */
    @Transactional
    @Override
    public void Delete(Integer id) throws ValidatorException {
        log.info("delete coffees w/ id {} - method entered", id);
        Coffee coffee = findOneWithOrders(id);
        coffeeDbRepository.delete(coffee);
    }

    /**
     * Returns a set of all coffees
     * @return {@code Set<Coffee>}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<Coffee> getAll() {
        log.info("getAll coffees - method entered");
        return coffeeDbRepository.findAllWithOrders();
    }

    /**
     * Returns a coffee by its id
     * @param id: Integer, id of coffee
     * @return Coffee
     * @throws ValidatorException
     *      if the coffee does not exist or id is null
     */
    private Coffee findOneWithOrders(Integer id) throws ValidatorException {
        log.info("getOne coffees w/ id {} - method entered", id);
        if (id == null) throw new CoffeeValidatorException("Invalid coffee id");
        return coffeeDbRepository.findByIdWithOrders(id).orElseThrow(() -> new CoffeeValidatorException("Invalid coffee id"));
    }

}
