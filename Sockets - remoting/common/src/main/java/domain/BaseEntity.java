package domain;

import lombok.*;

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
public abstract class BaseEntity<ID> implements Serializable {

    protected ID id = null;

    @Override
    public String toString() {
        return "BaseEntity(" +
                "id=" + id +
                ')';
    }

}
