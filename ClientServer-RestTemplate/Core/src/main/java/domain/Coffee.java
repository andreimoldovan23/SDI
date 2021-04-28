package domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Coffee Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true, exclude = {"shopOrders"})
@EqualsAndHashCode(exclude = {"quantity", "price", "shopOrders"}, callSuper = true)

@Entity
public class Coffee extends BaseEntity<Integer> {

    private String name;
    private String origin;
    private Integer quantity;
    private Integer price;

    @OneToMany(mappedBy = "coffee", fetch = FetchType.EAGER)
    private List<ShopOrder> shopOrders = new ArrayList<>();

    @Builder(builderMethodName = "Builder")
    private Coffee(Integer id, String name, String origin, Integer quantity, Integer price) {
        super(id);
        this.name = name;
        this.origin = origin;
        this.quantity = quantity;
        this.price = price;
    }

}