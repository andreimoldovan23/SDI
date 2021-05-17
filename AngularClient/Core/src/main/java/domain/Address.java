package domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Address entity
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true, exclude = "clients")
@EqualsAndHashCode(callSuper=true, exclude = "clients")

@Entity
@Table(indexes = {
        @Index(name = "addressUnique", columnList = "city, street, number", unique = true)
})
public class Address extends BaseEntity<Integer> {
    private String city;
    private String street;
    private Integer number;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "address")
    @Builder.Default
    private Set<Client> clients = new HashSet<>();
}
