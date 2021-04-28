package services;

import domain.Coffee;
import domain.Message;
import domain.Order;
import domain.Status;
import org.apache.commons.lang3.tuple.Pair;
import tcp.TcpClient;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class OrderService extends Service<Pair<Integer, Integer>, Order> implements IOrderService {

        /**
         * Constructor for CoffeeService
         */
        public OrderService(ExecutorService executorService, TcpClient tcpClient) {

                super(executorService, tcpClient);
        }

        /**
         * Returns all the coffees a client has ever ordered
         * @param clientId: the client whose coffees we want to see
         * @return {@code CompletableFuture<Set<Coffee>>} a set of all the coffees associated with that client
         */
        public CompletableFuture<Set<Coffee>> filterClientCoffees(Integer clientId) {
                return CompletableFuture.supplyAsync(() -> {
                        Message request = new Message();
                        request.setHeader("filterClientCoffees");
                        request.getBody().add(clientId);
                        Message response= tcpClient.sendAndReceive(request);
                        Set<Coffee> coffees = new HashSet<>();
                        response.getBody().forEach(o -> coffees.add((Coffee) o));
                        return coffees;
                }, executorService);

        }

        /**
         * Prints all the orders associated with a client
         * @param clientId: the client whose orders we want to see
         * @return {@code CompletableFuture<Set<Order>>} a set of orders associated with that client
         */
        public CompletableFuture<Set<Order>> filterClientOrders(Integer clientId) {
                return CompletableFuture.supplyAsync(() -> {
                        Message request = new Message();
                        request.setHeader("filterClientOrders");
                        request.getBody().add(clientId);
                        Message response= tcpClient.sendAndReceive(request);
                        Set<Order> orders = new HashSet<>();
                        response.getBody().forEach(o -> orders.add((Order) o));
                        return orders;
                }, executorService);

        }

        /**
         * Gets the coffees that have a substring in their name in a set
         * @param d1 of type LocalDateTime
         * @param d2 of type LocalDateTime
         */
        public void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("deleteOrderByDate");
                        request.getBody().add(d1);
                        request.getBody().add(d2);
                        tcpClient.sendAndReceive(request);
                });


        }

        /**
         * Creates a new order for a client and inserts into it a coffee product
         * @param id: The id of the order
         * @param clientId: The id of the client
         * @param coffeeId: The id of the coffee
         */
        public void buyCoffee(Integer id, Integer clientId, Integer coffeeId) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("buyCoffee");
                        request.getBody().add(id);
                        request.getBody().add(clientId);
                        request.getBody().add(coffeeId);
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Changes the status of an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param status: The new status
         */
        public void changeStatus(Integer ordId, Integer clientId, Status status) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("changeStatus");
                        request.getBody().add(ordId);
                        request.getBody().add(clientId);
                        request.getBody().add(status);
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Removes multiple coffees from an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be removed
         */
        public void deleteCoffeesFromOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("deleteCoffeesFromOrder");
                        request.getBody().add(ordId);
                        request.getBody().add(clientId);
                        coffeesId.forEach(c -> request.getBody().add(c));
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Adds multiple coffees to an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be added
         */
        public void addCoffeesToOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("addCoffeesToOrder");
                        request.getBody().add(ordId);
                        request.getBody().add(clientId);
                        coffeesId.forEach(c -> request.getBody().add(c));
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Adds an element to the repository
         * @param element of type entity
         */
        @Override
        public void Add(Order element) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("addOrder");
                        request.getBody().add(element);
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Updates an element from the repository
         * @param element of type entity
         */

        @Override
        public void Update(Order element) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("updateOrder");
                        request.getBody().add(element);
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Deletes an element from the repository
         * @param id of type Pair<Integer, Integer>
         */
        @Override
        public void Delete(Pair<Integer, Integer> id) {
                executorService.execute(()-> {
                        Message request = new Message();
                        request.setHeader("deleteOrder");
                        request.getBody().add(id.getLeft());
                        request.getBody().add(id.getRight());
                        tcpClient.sendAndReceive(request);
                });

        }

        /**
         * Gets all entities in a set
         * @return {@code CompletableFuture<Set<Order>>} a future with a set of orders
         */
        @Override
        public CompletableFuture<Set<Order>> getAll() {
                return CompletableFuture.supplyAsync(() -> {
                        Message request = new Message();
                        request.setHeader("getAllOrders");
                        Set<Order> orders = new HashSet<>();
                        tcpClient.sendAndReceive(request).getBody().forEach(o -> orders.add((Order) o));
                        return orders;
                }, executorService);
        }

}
