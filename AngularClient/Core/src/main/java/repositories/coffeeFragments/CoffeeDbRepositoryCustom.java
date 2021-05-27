package repositories.coffeeFragments;

import domain.Coffee;

import java.util.Set;

public interface CoffeeDbRepositoryCustom {
    Set<Coffee> filterCoffeesByName(String name);
    Set<Coffee> filterCoffeesByOrigin(String origin);
}
