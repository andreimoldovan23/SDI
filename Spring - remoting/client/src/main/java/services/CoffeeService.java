package services;

import domain.Coffee;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CoffeeService extends Service<Integer, Coffee> implements ICoffeeService {

    private final ICoffeeService coffeeService;

    /**
     * Constructor for CoffeeService
     */
    public CoffeeService(ICoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }


    /**
     * Gets the coffees that have a substring in their name in a set
     * @param name of type String
     * @return {@code Set<Coffee>} a set of coffees
     */
    public Set<Coffee> filterCoffeesByName(String name) {
        return coffeeService.filterCoffeesByName(name);
    }

    /**
     * Gets the coffees that have a substring in their origin in a set
     * @param origin: string
     * @return {@code Set<Coffee>} a set of coffees
     */
    public Set<Coffee> filterCoffeesByOrigin(String origin) {
        return coffeeService.filterCoffeesByOrigin(origin);
    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Coffee element) {
        coffeeService.Add(element);
    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Coffee element) {
        coffeeService.Update(element);
    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    @Override
    public void Delete(Integer id) {
        coffeeService.Delete(id);
    }

    /**
     * Gets all entities in a set
     * @return {@code Set<Coffee>} a set of coffees
     */
    @Override
    public Set<Coffee> getAll() {
        return coffeeService.getAll();
    }

}
