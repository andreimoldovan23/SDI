package service;

import domain.Client;
import domain.Coffee;
import domain.Order;
import domain.Status;
import domain.Validators.ClientValidatorException;
import domain.Validators.CoffeeValidatorException;
import domain.Validators.OrderValidatorException;
import domain.Validators.ValidatorException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import repository.Repository;
import services.IOrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OrderService  extends Service<Pair<Integer, Integer>, Order> implements IOrderService {
        private final Repository<Integer, Coffee> coffeeRepository;
        private final Repository<Integer, Client> clientRepository;

        /**
         * Constructor for OrderService
         * @param constructorRepo of type {@code Repository<Pair<Integer, Integer>, Order>>}
         * @param coffeeRepository of type {@code Repository<Integer, Coffee>}
         * @param clientRepository of type {@code Repository<Integer, Client>}
         */
        public OrderService(Repository<Pair<Integer, Integer>, Order> constructorRepo,
                            Repository<Integer, Coffee> coffeeRepository,
                            Repository<Integer, Client> clientRepository) {
                super(constructorRepo);
                this.coffeeRepository = coffeeRepository;
                this.clientRepository = clientRepository;
        }

        /**
         * Returns all the coffees a client has ever ordered
         * @param clientId : the client whose coffees we want to see
         * @return {@code CompletableFuture<Set<Coffee>>} a set of all the coffees associated with that client
         */
        public CompletableFuture<Set<Coffee>> filterClientCoffees(Integer clientId) {
                return CompletableFuture.completedFuture(
                        StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .filter(order -> order.getId().getRight().equals(clientId))
                        .map(Order::getCoffeesId)
                        .map(coffeesIdList -> {
                            List<Coffee> coffeeList = new ArrayList<>();
                            for (Integer coffeeId: coffeesIdList) {
                                this.coffeeRepository.findOne(coffeeId).ifPresent(coffeeList::add);
                            }
                            return coffeeList;
                        })
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet()));
        }

        /**
         * Prints all the orders associated with a client
         * @param clientId : the client whose orders we want to see
         * @return {@code CompletableFuture<Set<Order>>} a set of orders associated with that client
         */
        public CompletableFuture<Set<Order>> filterClientOrders(Integer clientId) {
                return CompletableFuture.completedFuture(
                        StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .filter(order -> order.getId().getRight().equals(clientId))
                        .collect(Collectors.toSet()));
        }

        /**
         * Gets the coffees that have a substring in their name in a set
         * @param d1 of type LocalDateTime
         * @param d2 of type LocalDateTime
         */
        public void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2) {
                StreamSupport.stream(this.repository.findAll().spliterator(), false)
                        .filter(order -> order.getTime().isAfter(d1) && order.getTime().isBefore(d2))
                        .forEach(ord -> repository.delete(ord.getId())
                        );
        }

        /**
         * Creates a new order for a client and inserts into it a coffee product
         * @param id: The id of the order
         * @param clientId: The id of the client
         * @param coffeeId: The id of the coffee
         */
        public void buyCoffee(Integer id, Integer clientId, Integer coffeeId) {
            this.clientRepository.findOne(clientId).ifPresentOrElse(client -> this.coffeeRepository.findOne(coffeeId)
                            .ifPresentOrElse(coffee -> {
                                Order order = Order.Builder()
                                        .id(new ImmutablePair<>(id, clientId))
                                        .status(coffee.getQuantity() == null || coffee.getQuantity().equals(0) ? Status.outOfStock : Status.pending)
                                        .time(LocalDateTime.now())
                                        .build();
                                order.getCoffeesId().add(coffeeId);
                                this.repository.save(order);
                            }, () -> { throw new CoffeeValidatorException("No such coffee"); }),
                    () -> { throw new ClientValidatorException("No such client"); });

        }

        /**
         * Changes the status of an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param status: The new status
         */
        public void changeStatus(Integer ordId, Integer clientId, Status status) {
                this.repository.findOne(new ImmutablePair<>(ordId, clientId))
                        .ifPresentOrElse(ord -> {
                            Status oldStatus = ord.getStatus();
                            ord.setStatus(status);
                            try {
                                repository.update(ord);
                            } catch (ValidatorException ve) {
                                ord.setStatus(oldStatus);
                            }
                        }, () -> {
                            throw new OrderValidatorException("No such order");
                        });
        }

        /**
         * Removes multiple coffees from an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be removed
         */
        public void deleteCoffeesFromOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                this.repository.findOne(new ImmutablePair<>(ordId, clientId))
                        .ifPresentOrElse(ord -> {
                            ord.getCoffeesId().removeAll(coffeesId);
                            try {
                                repository.update(ord);
                            } catch (ValidatorException ve) {
                                ord.getCoffeesId().addAll(coffeesId);
                            }
                        }, () -> {
                            throw new OrderValidatorException("No such order");
                        });
        }

        /**
         * Adds multiple coffees to an order
         * @param ordId: The id of the order
         * @param clientId: The id of the client
         * @param coffeesId: The coffee ids to be added
         */
        public void addCoffeesToOrder(Integer ordId, Integer clientId, List<Integer> coffeesId) {
                this.repository.findOne(new ImmutablePair<>(ordId, clientId))
                        .ifPresentOrElse(ord -> {
                            ord.getCoffeesId().addAll(coffeesId);
                            try {
                                repository.update(ord);
                            } catch (ValidatorException ve) {
                                ord.getCoffeesId().removeAll(coffeesId);
                            }
                        }, () -> {
                            throw new OrderValidatorException("No such order");
                        });
        }

}
