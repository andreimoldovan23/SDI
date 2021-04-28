import domain.*;
import domain.Validators.AddressValidator;
import domain.Validators.ClientValidator;
import domain.Validators.CoffeeValidator;
import domain.Validators.OrderValidator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import repository.AddressDbRepository;
import repository.ClientDbRepository;
import repository.CoffeeDbRepository;
import repository.OrderDbRepository;
import service.AddressService;
import service.ClientService;
import service.CoffeeService;
import service.OrderService;
import services.*;
import tcp.TcpServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    private static TcpServer tcpServer;

    private static IClientService clientService;
    private static IAddressService addressService;
    private static ICoffeeService coffeeService;
    private static IOrderService orderService;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        tcpServer = new TcpServer(executorService, ICrudService.PORT);

        ClientDbRepository clientDbRepository = new ClientDbRepository(new ClientValidator(), System.getenv("DBPATH"));
        clientService = new ClientService(clientDbRepository);

        AddressDbRepository addressDbRepository = new AddressDbRepository(new AddressValidator(), System.getenv("DBPATH"));
        addressService = new AddressService(addressDbRepository);

        CoffeeDbRepository coffeeDbRepository = new CoffeeDbRepository(new CoffeeValidator(), System.getenv("DBPATH"));
        coffeeService = new CoffeeService(coffeeDbRepository);

        OrderDbRepository orderDbRepository = new OrderDbRepository(new OrderValidator(), System.getenv("DBPATH"));
        orderService = new OrderService(orderDbRepository, coffeeDbRepository, clientDbRepository);

        handlerAdder();

        tcpServer.startServer();

    }

    @SuppressWarnings("DuplicatedCode")
    private static void handlerAdder() {
        //----------------Handlers for Client Service----------------//
        tcpServer.addHandler("addClient", request -> {
            Message message = new Message();
            try {
                clientService.Add((Client) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteClient", request -> {
            Message message = new Message();
            try {
                clientService.Delete((Integer) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("updateClient", request -> {
            Message message = new Message();
            try {
                clientService.Update((Client) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("getAllClients", request -> {
            Message message = new Message();
            try {
                Set<Client> res = clientService.getAll().get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterClientsByName", request -> {
            Message message = new Message();
            try {
                Set<Client> res = clientService.filterClientsByName((String) request.getBody().get(0)).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterClientsInAgeInterval", request -> {
            Message message = new Message();
            try {
                Integer age1 = (Integer) request.getBody().get(0);
                Integer age2 = (Integer) request.getBody().get(1);

                Set<Client> res = clientService.filterClientsInAgeInterval(age1, age2).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        //----------------Handlers for Address Service----------------//
        tcpServer.addHandler("addAddress", request -> {
            Message message = new Message();
            try {
                addressService.Add((Address) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteAddress", request -> {
            Message message = new Message();
            try {
                addressService.Delete((Integer) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("updateAddress", request -> {
            Message message = new Message();
            try {
                addressService.Update((Address) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("getAllAddresses", request -> {
            Message message = new Message();
            try {
                Set<Address> res = addressService.getAll().get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteAddressWithNumber", request -> {
            Message message = new Message();
            try {
                addressService.deleteAddressWithNumber((Integer) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        //----------------Handlers for Coffee Service----------------//
        tcpServer.addHandler("addCoffee", request -> {
            Message message = new Message();
            try {
                coffeeService.Add((Coffee) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteCoffee", request -> {
            Message message = new Message();
            try {
                coffeeService.Delete((Integer) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("updateCoffee", request -> {
            Message message = new Message();
            try {
                coffeeService.Update((Coffee) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("getAllCoffees", request -> {
            Message message = new Message();
            try {
                Set<Coffee> res = coffeeService.getAll().get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterCoffeesByName", request -> {
            Message message = new Message();
            try {
                Set<Coffee> res = coffeeService.filterCoffeesByName((String) request.getBody().get(0)).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterCoffeesByOrigin", request -> {
            Message message = new Message();
            try {
                Set<Coffee> res = coffeeService.filterCoffeesByOrigin((String) request.getBody().get(0)).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        //----------------Handlers for Order Service----------------//
        tcpServer.addHandler("addOrder", request -> {
            Message message = new Message();
            try {
                orderService.Add((Order) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteOrder", request -> {
            Message message = new Message();
            try {
                orderService.Delete(new ImmutablePair<>((Integer)request.getBody().get(0), (Integer)request.getBody().get(1)));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("updateOrder", request -> {
            Message message = new Message();
            try {
                orderService.Update((Order) request.getBody().get(0));
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("getAllOrders", request -> {
            Message message = new Message();
            try {
                Set<Order> res = orderService.getAll().get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterClientCoffees", request -> {
            Message message = new Message();
            try {
                Set<Coffee> res = orderService.filterClientCoffees((Integer) request.getBody().get(0)).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("filterClientOrders", request -> {
            Message message = new Message();
            try {
                Set<Order> res = orderService.filterClientOrders((Integer) request.getBody().get(0)).get();
                message.setHeader(Message.OK);
                for(var c: res)
                    message.getBody().add(c);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteOrderByDate", request -> {
            Message message = new Message();
            try {
                LocalDateTime d1 = (LocalDateTime) request.getBody().get(0);
                LocalDateTime d2 = (LocalDateTime) request.getBody().get(1);
                orderService.deleteOrderByDate(d1, d2);
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("buyCoffee", request -> {
            Message message = new Message();
            try {
                Integer id1 = (Integer) request.getBody().get(0);
                Integer id2 = (Integer) request.getBody().get(1);
                Integer id3 = (Integer) request.getBody().get(2);
                orderService.buyCoffee(id1, id2, id3);
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("changeStatus", request -> {
            Message message = new Message();
            try {
                Integer id1 = (Integer) request.getBody().get(0);
                Integer id2 = (Integer) request.getBody().get(1);
                Status status = (Status) request.getBody().get(2);
                orderService.changeStatus(id1, id2, status);
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("deleteCoffeesFromOrder", request -> {
            Message message = new Message();
            try {
                Integer id1 = (Integer) request.getBody().get(0);
                Integer id2 = (Integer) request.getBody().get(1);
                List<Integer> coffeeIds = new ArrayList<>();
                request.getBody().subList(2, request.getBody().size()).forEach(o -> coffeeIds.add((Integer) o));
                orderService.deleteCoffeesFromOrder(id1, id2, coffeeIds);
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });

        tcpServer.addHandler("addCoffeesToOrder", request -> {
            Message message = new Message();
            try {
                Integer id1 = (Integer) request.getBody().get(0);
                Integer id2 = (Integer) request.getBody().get(1);
                List<Integer> coffeeIds = new ArrayList<>();
                request.getBody().subList(2, request.getBody().size()).forEach(o -> coffeeIds.add((Integer) o));
                orderService.addCoffeesToOrder(id1, id2, coffeeIds);
                message.setHeader(Message.OK);
            } catch (Exception e) {
                message.setHeader(Message.ERROR);
                message.getBody().add(e.getMessage());
            }
            return message;
        });
    }
}
