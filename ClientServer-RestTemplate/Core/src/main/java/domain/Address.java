package domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


/**
 * Address entity
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)

@Entity
public class Address extends BaseEntity<Integer> {

    private String city;
    private String street;
    private Integer number;

    @OneToMany(mappedBy = "address", fetch = FetchType.EAGER)
    private List<Client> clients = new ArrayList<>();

    @Builder(builderMethodName = "Builder")
    private Address(Integer id, String city, String street, Integer number) {
        super(id);
        this.city = city;
        this.street = street;
        this.number = number;
    }

}
