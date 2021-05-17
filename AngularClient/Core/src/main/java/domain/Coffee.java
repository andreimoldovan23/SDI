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
@EqualsAndHashCode(exclude = {"quantity", "price", "orders"}, callSuper = true)

@Entity
@Table(indexes = {
        @Index(name = "coffeeUnique", columnList = "name, origin", unique = true)
})
public class Coffee extends BaseEntity<Integer> {
    private String name;
    private String origin;
    private Integer quantity;
    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "coffee")
    @Builder.Default
    private Set<ShopOrder> orders = new HashSet<>();
}