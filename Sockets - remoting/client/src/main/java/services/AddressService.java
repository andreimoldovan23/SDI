package services;

import domain.Address;
import domain.Message;
import tcp.TcpClient;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class AddressService extends Service<Integer, Address> implements IAddressService {

    /**
     * Constructor for AddressService
     */
    public AddressService(ExecutorService executorService, TcpClient tcpClient) {

        super(executorService, tcpClient);
    }


    /**
     * Deletes all addresses with a given street number
     * @param number: integer
     */
    public void deleteAddressWithNumber(Integer number)
    {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("deleteAddressWithNumber");
            request.getBody().add(number);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    @Override
    public void Add(Address element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("addAddress");
            request.getBody().add(element);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */

    @Override
    public void Update(Address element) {
        executorService.execute(()-> {
            Message request = new Message();
            request.setHeader("updateAddress");
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
            request.setHeader("deleteAddress");
            request.getBody().add(id);
            tcpClient.sendAndReceive(request);
        });

    }

    /**
     * Gets all entities in a set
     * @return {@code Set<entity>}
     */
    @Override
    public CompletableFuture<Set<Address>> getAll() {
        return CompletableFuture.supplyAsync(() -> {
            Message request = new Message();
            request.setHeader("getAllAddresses");
            Set<Address> addresses = new HashSet<>();
            tcpClient.sendAndReceive(request).getBody().forEach(o -> addresses.add((Address) o));
            return addresses;
        }, executorService);
    }
}
