package services.interfaces;

import domain.BaseEntity;
import domain.Validators.ValidatorException;

import java.util.Set;

public interface ICrudService<ID, T extends BaseEntity<ID>> {
    void Add(T entity) throws ValidatorException;
    void Update(T entity) throws ValidatorException;
    void Delete(ID id) throws ValidatorException;
    Set<T> getAll();
}
