package services;

import domain.Coffee;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface ICoffeeService extends ICrudService<Integer, Coffee> {
    CompletableFuture<Set<Coffee>> filterCoffeesByName(String name);
    CompletableFuture<Set<Coffee>> filterCoffeesByOrigin(String origin);
}
