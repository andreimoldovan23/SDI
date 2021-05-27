package services.implementations;

import domain.Client;
import domain.Coffee;
import domain.ShopOrder;
import domain.Status;
import domain.Validators.ClientValidatorException;
import domain.Validators.ValidatorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import repositories.clientFragments.ClientDbRepository;
import repositories.coffeeFragments.CoffeeDbRepository;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final CoffeeDbRepository coffeeRepository;
    private final ClientDbRepository clientRepository;

    /**
     * Returns all the coffees a client has ever ordered
     * @param clientId : Integer, the id of the client whose coffees we want to see
     * @return {@code Set<Coffee>} a set of all the coffees associated with that client
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<Coffee> filterClientCoffees(Integer clientId) {
        log.info("filterClientCoffees - method entered - client={}", clientId);
        Client client = clientRepository.findByIdWithOrders(clientId).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
        Set<Coffee> clientCoffees = client.getOrders().stream()
                .map(ShopOrder::getCoffee)
                .collect(Collectors.toSet());
        log.info("filterClientCoffees result={} - method finished", clientCoffees);
        return clientCoffees;
    }

    /**
     * Returns all the orders associated with a client
     * @param clientId : Integer, the id of the client whose orders we want to see
     * @return {@code Set<ShopOrder>} a set of orders associated with that client
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<ShopOrder> filterClientOrders(Integer clientId) {
        log.info("filterClientOrders - method entered - client={}", clientId);
        Client client = clientRepository.findByIdWithOrders(clientId).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
        Set<ShopOrder> clientOrders = new HashSet<>(client.getOrders());
        log.info("filterClientOrders result = {} - method finished", clientOrders);
        return clientOrders;
    }

    /**
     * Deletes all orders in a given time interval
     * @param d1: LocalDateTime, low interval limit
     * @param d2: LocalDateTime, high interval limit
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2) {
        log.info("deleteOrderByDate - method entered - firstDate={}, secondDate={}", d1, d2);
        clientRepository.findAllWithOrders()
                .forEach(client -> {
                    List<ShopOrder> orders = client.getOrders().stream()
                            .filter(shopOrder -> shopOrder.getTime().isAfter(d1) && shopOrder.getTime().isBefore(d2))
                            .collect(Collectors.toList());

                    client.getOrders().removeAll(orders);
                    clientRepository.save(client);
                });
    }

    /**
     * Creates a new order with a client and coffee
     * @param clientId: The id of the client
     * @param coffeeId: The id of the coffee
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void buyCoffee(Integer clientId, Integer coffeeId) throws ValidatorException {
        log.info("buyCoffee - method entered - client={}, coffee={}", clientId, coffeeId);

        Client client = clientRepository.findByIdWithOrders(clientId).orElseThrow(() -> new ClientValidatorException("Invalid client id"));
        Coffee coffee = coffeeRepository.findByIdWithOrders(coffeeId).orElseThrow(() -> new ClientValidatorException("Invalid coffee id"));

        ShopOrder shopOrder = ShopOrder.builder()
                .coffee(coffee)
                .client(client)
                .status(coffee.getQuantity() == null || coffee.getQuantity().equals(0) ? Status.outOfStock : Status.pending)
                .time(LocalDateTime.now())
                .build();
        client.getOrders().add(shopOrder);
        clientRepository.save(client);
        log.info("buyCoffee newOrder={} - method finished", shopOrder);
    }

    /**
     * Changes the status of an order
     * @param ordId: The id of the order
     * @param status: The new status
     */
    @Transactional
    @Override
    public void changeStatus(Integer ordId, Status status) throws ValidatorException {
        log.info("changeStatus order - method entered w/ id {}, status {}", ordId, status);
        clientRepository.findAllWithOrders()
                .forEach(client -> {
                    Set<ShopOrder> orders = client.getOrders();
                    orders.forEach(ord -> {
                        if (ord.getId().equals(ordId)) {
                            ord.setStatus(status);
                            clientRepository.save(client);
                        }
                    });
                });
    }

    /**
     * Returns a set of all orders
     * @return {@code Set<ShopOrder>}
     */
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    @Override
    public Set<ShopOrder> getAll() {
        return clientRepository.findAllWithOrders().stream()
                .flatMap(client -> client.getOrders().stream())
                .collect(Collectors.toSet());
    }

}
