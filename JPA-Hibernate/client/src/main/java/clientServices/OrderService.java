package clientServices;

import domain.Coffee;
import domain.ShopOrder;
import domain.Status;
import domain.Validators.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import services.IOrderService;

import java.time.LocalDateTime;
import java.util.Set;

@org.springframework.stereotype.Service
public class OrderService extends Service<Integer, ShopOrder> implements IOrderService {

        @Autowired
        private IOrderService orderService;

        /**
         * Returns all the coffees a client has ever ordered
         * @param client: the client whose coffees we want to see
         * @return {@code Set<Coffee>} a set of all the coffees associated with that client
         */
        public Set<Coffee> filterClientCoffees(Integer client) {
                return orderService.filterClientCoffees(client);
        }

        /**
         * Prints all the orders associated with a client
         * @param client: the client whose orders we want to see
         * @return {@code Set<ShopOrder>} a set of orders associated with that client
         */
        public Set<ShopOrder> filterClientOrders(Integer client) {
                return orderService.filterClientOrders(client);
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
         * @param client: The id of the client
         * @param coffee: The id of the coffee
         */
        public void buyCoffee(Integer id, Integer client, Integer coffee) {
                orderService.buyCoffee(id, client, coffee);
        }

        /**
         * Changes the status of an order
         * @param ordId: The id of the order
         * @param status: The new status
         */
        public void changeStatus(Integer ordId, Status status) {
                orderService.changeStatus(ordId, status);
        }

        /**
         * Adds an element to the repository
         * @param element of type entity
         */
        @Override
        public void Add(ShopOrder element) {
                orderService.Add(element);
        }

        /**
         * Updates an element from the repository
         * @param element of type entity
         */

        @Override
        public void Update(ShopOrder element) {
                orderService.Update(element);
        }

        /**
         * Deletes an element from the repository
         * @param id of type Pair<Integer, Integer>
         */
        @Override
        public void Delete(Integer id) {
                orderService.Delete(id);
        }

        /**
         * Gets all entities in a set
         * @return {@code Set<ShopOrder>} a future with a set of orders
         */
        public Set<ShopOrder> getAll() {
                return orderService.getAll();
        }

        @Override
        public ShopOrder findOneById(Integer integer) throws ValidatorException {
                return orderService.findOneById(integer);
        }

}
