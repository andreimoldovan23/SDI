package services;

import domain.Coffee;

import java.util.Set;

public interface ICoffeeService extends ICrudService<Integer, Coffee> {
    Set<Coffee> filterCoffeesByName(String name);
    Set<Coffee> filterCoffeesByOrigin(String origin);
}
