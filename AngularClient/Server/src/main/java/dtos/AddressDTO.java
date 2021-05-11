package dtos;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Integer id;
    private String city;
    private String street;
    private Integer number;
}
