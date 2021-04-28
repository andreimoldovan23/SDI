package domain;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base Entity
 * @param <ID> generic - the type of the id
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode

@MappedSuperclass
public abstract class BaseEntity<ID> implements Serializable {

    @Id
    protected ID id = null;

    @Override
    public String toString() {
        return "BaseEntity(" +
                "id=" + id +
                ')';
    }

}
