package domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Client entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true, exclude = {"address"})
@EqualsAndHashCode(callSuper = true, exclude = {"age", "phoneNumber", "address"})

@Entity
@Table(indexes = {
        @Index(name = "clientUnique", columnList = "firstName, lastName, address_id", unique = true)
})
public class Client extends BaseEntity<Integer> {
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private Integer age;
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "client")
    private List<ShopOrder> orders = new ArrayList<>();
}
