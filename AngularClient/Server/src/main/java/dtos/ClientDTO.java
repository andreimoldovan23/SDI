package dtos;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String phoneNumber;
    private AddressDTO address;
}
