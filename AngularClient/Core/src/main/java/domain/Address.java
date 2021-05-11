package domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Address entity
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)

@Entity
@Table(indexes = {
        @Index(name = "addressUnique", columnList = "city, street, number", unique = true)
})
public class Address extends BaseEntity<Integer> {
    private String city;
    private String street;
    private Integer number;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "address")
    private List<Client> clients = new ArrayList<>();
}
