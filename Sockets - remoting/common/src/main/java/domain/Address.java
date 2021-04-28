package domain;

import lombok.*;

/**
 * Address entity
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)
public class Address extends BaseEntity<Integer> {

    private String city = null;
    private String street = null;
    private Integer number = null;

    @Builder(builderMethodName = "Builder")
    private Address(Integer id, String city, String street, Integer number) {
        super(id);
        this.city = city;
        this.street = street;
        this.number = number;
    }

}
