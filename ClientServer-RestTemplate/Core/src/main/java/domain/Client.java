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
@ToString(callSuper = true, exclude = {"address", "shopOrders"})
@EqualsAndHashCode(callSuper = true, exclude = {"age", "phoneNumber", "address", "shopOrders"})

@Entity
public class Client extends BaseEntity<Integer> {

    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<ShopOrder> shopOrders = new ArrayList<>();

    private Integer age;
    private String phoneNumber;

    @Builder(builderMethodName = "Builder")
    private Client(Integer id, String firstName, String lastName, Address address, Integer age, String phoneNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

}
