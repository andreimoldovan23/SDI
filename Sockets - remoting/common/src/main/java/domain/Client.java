package domain;

import lombok.*;


/**
 * Client entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(exclude = {"age", "phoneNumber"}, callSuper = true)
public class Client extends BaseEntity<Integer> {

    private String firstName = null;
    private String lastName = null;
    private Integer addressId = null;
    private Integer age = null;
    private String phoneNumber = null;

    @Builder(builderMethodName = "Builder")
    private Client(Integer id, String firstName, String lastName, Integer addressId, Integer age, String phoneNumber) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.addressId = addressId;
    }

}
