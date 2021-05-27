package domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Coffee Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true, exclude = "orders")
@EqualsAndHashCode(exclude = {"orders"}, callSuper = true)

@Entity
@Table(indexes = {
        @Index(name = "coffeeUnique", columnList = "name, origin", unique = true)
})
@NamedEntityGraphs({
        @NamedEntityGraph(name = "graph.CoffeeWithOrders",
            attributeNodes = @NamedAttributeNode(value = "orders", subgraph = "subgraph.OrderClientCoffee"),
            subgraphs = @NamedSubgraph(name = "subgraph.OrderClientCoffee",
                attributeNodes = {
                    @NamedAttributeNode("client"), @NamedAttributeNode("coffee")
                }))
})
public class Coffee extends BaseEntity<Integer> {
    private String name;
    private String origin;
    private Integer quantity;
    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "coffee")
    @Builder.Default
    private Set<ShopOrder> orders = new HashSet<>();
}