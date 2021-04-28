package services;

import domain.Client;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface IClientService extends ICrudService<Integer, Client> {
    CompletableFuture<Set<Client>> filterClientsByName(String name);
    CompletableFuture<Set<Client>> filterClientsInAgeInterval(Integer age1, Integer age2);
}
