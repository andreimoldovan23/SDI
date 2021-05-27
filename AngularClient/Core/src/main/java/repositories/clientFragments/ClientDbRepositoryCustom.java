package repositories.clientFragments;

import domain.Client;

import java.util.Set;

public interface ClientDbRepositoryCustom {
    Set<Client> filterClientByFirstNameOrLastName(String name);
    Set<Client> filterClientByAgeInterval(Integer age1, Integer age2);
}
