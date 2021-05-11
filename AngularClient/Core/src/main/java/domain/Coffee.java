package domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Coffee Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(exclude = {"quantity", "price"}, callSuper = true)

@Entity
@Table(indexes = {
        @Index(name = "coffeeUnique", columnList = "name, origin", unique = true)
})
public class Coffee extends BaseEntity<Integer> {
    private String name;
    private String origin;
    private Integer quantity;
    private Integer price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coffee")
    private List<ShopOrder> orders = new ArrayList<>();
}