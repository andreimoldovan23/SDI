package services;

import domain.Client;
import domain.Message;
import tcp.TcpClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ClientService extends Service<Integer, Client> implements IClientService {

    /**
     * Constructor for ClientService
     */
    public ClientService(ExecutorService executorService, TcpClient tcpClient) {

        super(executorService, tcpClient);
    }


    /**
     * Gets the clients that have a substring in their name in a set
     * @param name of type String
     * @return {@code CompletableFuture<Set<Client>>} a future with a set of clients
    */
    public CompletableFuture<Set<Client>> filterClientsByName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("filterClientsByName");
            request.getBody().add(name);
            Message response= tcpClient.sendAndReceive(request);
            Set<Client> result = new HashSet<>();
            response.getBody().forEach(o -> result.add((Client) o));
            return result;
        }, executorService);
    }

    /**
     * Gets all clients with age in the given interval
     * @param age1: low age interval limit
     * @param age2: high age interval limit
     * @return {@code CompletableFuture<Set<Client>>} a future with a set of clients
     */
    public CompletableFuture<Set<Client>> filterClientsInAgeInterval(Integer age1, Integer age2) {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("filterClientsInAgeInterval");
            request.getBody().add(age1);
            request.getBody().add(age2);
            Message response= tcpClient.sendAndReceive(request);
            Set<Client> result = new HashSet<>();
            response.getBody().forEach(o -> result.add((Client) o));
            return result;
        }, executorService);

    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Client element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("addClient");
            request.getBody().add(element);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Client element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("updateClient");
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
        request.setHeader("deleteClient");
        request.getBody().add(id);
        tcpClient.sendAndReceive(request);
    });
    }

    /**
     * Gets all entities in a set
     * @return {@code CompletableFuture<Set<entity>>}
     */
    @Override
    public CompletableFuture<Set<Client>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("getAllClients");
            Set<Client> clients = new HashSet<>();
            tcpClient.sendAndReceive(request).getBody().forEach(o -> clients.add((Client) o));
            return clients;
        }, executorService);
    }

}
