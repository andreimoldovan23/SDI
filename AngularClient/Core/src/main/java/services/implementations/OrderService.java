package services.implementations;

import domain.Client;
import domain.Coffee;
import domain.ShopOrder;
import domain.Status;
import domain.Validators.ClientValidatorException;
import domain.Validators.OrderValidator;
import domain.Validators.OrderValidatorException;
import domain.Validators.ValidatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import repositories.ClientDbRepository;
import repositories.CoffeeDbRepository;
import repositories.OrderDbRepository;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@org.springframework.stereotype.Service
public class OrderService extends Service<Integer, ShopOrder> implements IOrderService {

    private final CoffeeDbRepository coffeeRepository;
    private final ClientDbRepository clientRepository;

    /**
     * Constructor for OrderService
     * @param constructorRepo of type {@code Repository<Pair<Integer, Integer>, ShopOrder>>}
     * @param coffeeRepository of type {@code Repository<Integer, Coffee>}
     * @param clientRepository of type {@code Repository<Integer, Client>}
     */
    public OrderService(OrderDbRepository constructorRepo, CoffeeDbRepository coffeeRepository,
                        ClientDbRepository clientRepository, OrderValidator orderValidator) {
        super(constructorRepo, orderValidator);
        this.coffeeRepository = coffeeRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Returns all the coffees a client has ever ordered
     * @param clientId : the client whose coffees we want to see
     * @return {@code Set<Coffee>} a set of all the coffees associated with that client
     */
    @Transactional
    public Set<Coffee> filterClientCoffees(Integer clientId) {
        log.info("filterClientCoffees - method entered - client={}", clientId);
        Set<Coffee> result = ((OrderDbRepository)this.repository).filterClientCoffees(clientId);
        log.info("filterClientCoffees result={} - method finished", result);
        return result;
    }

    /**
     * Prints all the orders associated with a client
     * @param clientId : the client whose orders we want to see
     * @return {@code Set<ShopOrder>} a set of orders associated with that client
     */
    @Transactional
    public Set<ShopOrder> filterClientOrders(Integer clientId) {
        log.info("filterClientOrders - method entered - client={}", clientId);
        Set<ShopOrder> result = ((OrderDbRepository)this.repository).filterClientOrders(clientId);
        log.info("filterClientOrders result = {} - method finished", result);
        return result;
    }

    /**
     * Gets the coffees that have a substring in their name in a set
     * @param d1 of type LocalDateTime
     * @param d2 of type LocalDateTime
     */
    @Transactional
    public void deleteOrderByDate(LocalDateTime d1, LocalDateTime d2) {
        log.info("deleteOrderByDate - method entered - firstDate={}, secondDate={}", d1, d2);
        List<ShopOrder> deletedOrders = new ArrayList<>();
        this.repository.findAll().stream()
                    .filter(order -> order.getTime().isAfter(d1) && order.getTime().isBefore(d2))
                    .forEach(ord -> {
                            deletedOrders.add(ord);
                            repository.deleteById(ord.getId());
                        }
                    );
        log.info("deleteOrderByDate deletedOrders = {} - method finished", deletedOrders);
    }

    /**
     * Creates a new order for a client and inserts into it a coffee product
     * @param clientId: The id of the client
     * @param coffeeId: The id of the coffee
     */
    @Transactional
    public void buyCoffee(Integer clientId, Integer coffeeId) throws ValidatorException {
        log.info("buyCoffee - method entered - client={}, coffee={}", clientId, coffeeId);

        Client client = clientRepository.findById(clientId).orElseThrow(() -> {
            log.info("buyCoffee - no such client");
            throw new ClientValidatorException("No such client");
        });

        Coffee coffee = coffeeRepository.findById(coffeeId).orElseThrow(() -> {
            log.info("buyCoffee - no such coffee");
            throw new ClientValidatorException("No such coffee");
        });

        ShopOrder shopOrder = ShopOrder.builder()
                .coffee(coffee)
                .client(client)
                .status(coffee.getQuantity() == null || coffee.getQuantity().equals(0) ? Status.outOfStock : Status.pending)
                .time(LocalDateTime.now())
                .build();
        this.repository.save(shopOrder);
        log.info("buyCoffee newOrder={} - method finished", shopOrder);
    }

    /**
     * Changes the status of an order
     * @param ordId: The id of the order
     * @param status: The new status
     */
    @Transactional
    public void changeStatus(Integer ordId, Status status) throws ValidatorException {
        log.info("changeStatus - method entered: ordId={}, status={}", ordId, status);
        this.repository.findById(ordId).ifPresentOrElse(ord -> {
            ord.setStatus(status);
            this.repository.save(ord);
            log.info("changeStatus - status changed - method finished");
        }, () -> {
            log.info("changeStatus - order doesn't exist");
            throw new OrderValidatorException("No such order");
        });
    }

    /**
     * Updates an element
     * @param element of type shoporder
     */
    @Transactional
    public void Update(ShopOrder element) throws ValidatorException {
        log.info("update order - method entered");
        validator.validate(element);
        repository.findById(element.getId()).ifPresentOrElse(elem ->
        {
            Client client = clientRepository.findById(element.getClient().getId()).orElse(null);
            Coffee coffee = coffeeRepository.findById(element.getCoffee().getId()).orElse(null);
            elem.setStatus(element.getStatus());
            elem.setTime(element.getTime());
            if(client != null) elem.setClient(client);
            if(coffee != null) elem.setCoffee(coffee);
            repository.save(elem);
        }, () -> {
            log.info("update order - throwing exception");
            throw new ValidatorException("No such element");
        });
        log.info("update order - method finished");
    }

}
