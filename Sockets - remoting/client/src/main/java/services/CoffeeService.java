package services;

import domain.Coffee;
import domain.Message;
import tcp.TcpClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class CoffeeService extends Service<Integer, Coffee> implements ICoffeeService {

    /**
     * Constructor for CoffeeService
     */
    public CoffeeService(ExecutorService executorService, TcpClient tcpClient) {

        super(executorService, tcpClient);
    }


    /**
     * Gets the coffees that have a substring in their name in a set
     * @param name of type String
     * @return {@code CompletableFuture<Set<Coffee>>} a future with a set of coffees
     */
    public CompletableFuture<Set<Coffee>> filterCoffeesByName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("filterCoffeesByName");
            request.getBody().add(name);
            Message response = tcpClient.sendAndReceive(request);
            Set<Coffee> coffees = new HashSet<>();
            response.getBody().forEach(o -> coffees.add((Coffee) o));
            return coffees;
        }, executorService);

    }

    /**
     * Gets the coffees that have a substring in their origin in a set
     * @param origin: string
     * @return {@code CompletableFuture<Set<Coffee>>} a future with a set of coffees
     */
    public CompletableFuture<Set<Coffee>> filterCoffeesByOrigin(String origin) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("filterCoffeesByOrigin");
            request.getBody().add(origin);
            Message response = tcpClient.sendAndReceive(request);
            Set<Coffee> coffees = new HashSet<>();
            response.getBody().forEach(o -> coffees.add((Coffee) o));
            return coffees;
        }, executorService);

    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Coffee element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("addCoffee");
            request.getBody().add(element);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Coffee element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("updateCoffee");
            request.getBody().add(element);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    @Override
    public void Delete(Integer id) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("deleteCoffee");
            request.getBody().add(id);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Gets all entities in a set
     * @return {@code CompletableFuture<Set<Coffee>>} a future with a set of coffees
     */
    @Override
    public CompletableFuture<Set<Coffee>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("getAllCoffees");
            Message response = tcpClient.sendAndReceive(request);
            Set<Coffee> coffees = new HashSet<>();
            response.getBody().forEach(o -> coffees.add((Coffee) o));
            return coffees;
        }, executorService);
    }

}
