package services.implementations;


import domain.BaseEntity;
import domain.Validators.Validator;
import domain.Validators.ValidatorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import services.interfaces.ICrudService;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public abstract class Service<ID, entity extends BaseEntity<ID>> implements ICrudService<ID, entity> {
    protected final JpaRepository<entity, ID> repository;
    protected final Validator<ID, entity> validator;

    /**
     * Adds an element to the repository
     * @param element of type entity
     */
    public void Add(entity element) throws ValidatorException {
        log.info("add - method entered");
        validator.validate(element);
        entity ent = repository.findById(element.getId()).orElse(null);
        if (ent != null) throw new RuntimeException("Element already present");
        repository.save(element);
        log.info("add - method finished");
    }

    /**
     * Updates an element from the repository
     * @param element of type entity
     */
    @Transactional
    public void Update(entity element) throws ValidatorException {
        log.info("update - method entered");
        validator.validate(element);
        repository.findById(element.getId()).ifPresentOrElse(id ->
                repository.save(element), () -> {
            log.info("update - throwing exception");
            throw new ValidatorException("No such element");
        });
        log.info("update - method finished");
    }

    /**
     * Deletes an element from the repository
     * @param id of type ID
     */
    public void Delete(ID id) throws ValidatorException {
        log.info("delete - method entered");
        repository.deleteById(id);
        log.info("delete - method finished");
    }

    /**
     * Gets all entities in a set
     * @return {@code CompletableFuture<Set<entity>>}
     */
    public Set<entity> getAll() {
        log.info("getAll - method entered");
        Set<entity> result =  new HashSet<>(this.repository.findAll());
        log.info("getAll - method finished");
        return result;
    }

    public entity findOneById(ID id) {
        log.info("findOneById - method entered with " + id.toString());
        return this.repository.findById(id).orElseThrow(() -> {
            log.info("findOneById - throwing exception");
            throw new ValidatorException("No such entity");
        });
    }

}
