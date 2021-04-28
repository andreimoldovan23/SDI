package services;

import domain.Coffee;
import domain.Order;
import domain.Status;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class OrderService extends Service<Pair<Integer, Integer>, Order> implements IOrderService {

        private final IOrderService orderService;

        /**
         * Constructor for CoffeeService
         */
        public OrderService(IOrderService orderService) {
                this.orderService = orderService;
        }

        /**
         * Returns all the coffees a client has ever ordered
         * @param clientId: the client whose coffees we want to see
         * @return {@code Set<Coffee>} a set of all the coffees associated with that client
         */
        public Set<Coffee> filterClientCoffees(Integer clientId) {
                return orderService.filterClientCoffees(clientId);
        }

        /**
         * Prints all the orders associated with a client
         * @param clientId: the client whose orders we want to see
         * @return {@code Set<Order>} a set of orders associated with that client
         */
        public Set<Order> filterClientOrders(Integer clientId) {
                return orderService.filterClientOrders(clientId);
        }

        /**
         * Gets the coffees that have a substring in their name in a set
         * @param d1 of type LocalDateTime
         * @param d2 of type LocalDateTime
         */
        public void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2) {
                orderService.deleteOrderByDate(d1, d2);
        }

        /**
         * Creates a new order for a client and inserts into it a coffee product
         * @param id: The id of the order
         * @param clientId: The id of the client
         * @param coffeeId: The id of the coffee
         */
        public void buyCoffee(Integer id, Integer clientId, Integer coffeeId) {
                orderService.buyCoffee(id, clientId, coffeeId);
        }

        /**
         * Changes the status of an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param status: The new status
         */
        public void changeStatus(Integer ordId, Integer clientId, Status status) {
                orderService.changeStatus(ordId, clientId, status);
        }

        /**
         * Removes multiple coffees from an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be removed
         */
        public void deleteCoffeesFromOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                orderService.deleteCoffeesFromOrder(ordId, clientId, coffeesId);
        }

        /**
         * Adds multiple coffees to an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be added
         */
        public void addCoffeesToOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                orderService.addCoffeesToOrder(ordId, clientId, coffeesId);
        }

        /**
         * Adds an element to the repository
         * @param element of type entity
         */
        @Override
        public void Add(Order element) {
                orderService.Add(element);
        }

        /**
         * Updates an element from the repository
         * @param element of type entity
         */

        @Override
        public void Update(Order element) {
                orderService.Update(element);
        }

        /**
         * Deletes an element from the repository
         * @param id of type Pair<Integer, Integer>
         */
        @Override
        public void Delete(Pair<Integer, Integer> id) {
                orderService.Delete(id);
        }

        /**
         * Gets all entities in a set
         * @return {@code Set<Order>} a future with a set of orders
         */
        public Set<Order> getAll() {

                return orderService.getAll();
        }

}
