package domain;

import lombok.*;

/**
 * Coffee Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(exclude = {"quantity", "price"}, callSuper = true)
public class Coffee extends BaseEntity<Integer> {

    private String name = null;
    private String origin = null;
    private Integer quantity = null;
    private Integer price = null;

    @Builder(builderMethodName = "Builder")
    private Coffee(Integer id, String name, String origin, Integer quantity, Integer price) {
        super(id);
        this.name = name;
        this.origin = origin;
        this.quantity = quantity;
        this.price = price;
    }

}