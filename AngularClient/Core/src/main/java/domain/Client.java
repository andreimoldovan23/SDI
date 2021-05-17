package domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Client entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true, exclude = {"address", "orders"})
@EqualsAndHashCode(callSuper = true, exclude = {"clientInfo", "address", "orders"})

@Entity
@Table(indexes = {
        @Index(name = "clientUnique", columnList = "firstName, lastName, address_id", unique = true)
})
public class Client extends BaseEntity<Integer> {
    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address address;

    @Embedded
    @Builder.Default
    private ClientInfo clientInfo = new ClientInfo();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "client")
    @Builder.Default
    private Set<ShopOrder> orders = new HashSet<>();

    public Integer getAge() {
        return clientInfo.getAge();
    }

    public void setAge(Integer newAge) {
        clientInfo.setAge(newAge);
    }

    public String getPhoneNumber() {
        return clientInfo.getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        clientInfo.setPhoneNumber(phoneNumber);
    }
}
