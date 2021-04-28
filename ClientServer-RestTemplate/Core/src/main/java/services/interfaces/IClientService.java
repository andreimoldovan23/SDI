package services.interfaces;

import domain.Client;

import java.util.Set;

public interface IClientService extends ICrudService<Integer, Client> {
    Set<Client> filterClientsByName(String name);
    Set<Client> filterClientsInAgeInterval(Integer age1, Integer age2);
}
