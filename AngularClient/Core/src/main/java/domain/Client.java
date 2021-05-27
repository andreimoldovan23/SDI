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
@ToString(callSuper = true, exclude = {"orders"})
@EqualsAndHashCode(callSuper = true, exclude = {"orders"})

@Entity
@Table(indexes = {
        @Index(name = "clientUnique", columnList = "firstName, lastName, address_id", unique = true)
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "graph.ClientWithAddress",
            attributeNodes = @NamedAttributeNode(value = "address", subgraph = "subgraph.AddressClients"),
            subgraphs = @NamedSubgraph(name = "subgraph.AddressClients",
                attributeNodes = @NamedAttributeNode("clients"))),

        @NamedEntityGraph(name = "graph.ClientWithOrders",
            attributeNodes = @NamedAttributeNode(value = "orders", subgraph = "subgraph.OrdersClientCoffee"),
            subgraphs = @NamedSubgraph(name = "subgraph.OrdersClientCoffee",
                    attributeNodes = {
                        @NamedAttributeNode("client"), @NamedAttributeNode("coffee")
                    }))
})
public class Client extends BaseEntity<Integer> {
    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id")
    private Address address;

    @Embedded
    @Builder.Default
    private ClientInfo clientInfo = new ClientInfo();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "client")
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
