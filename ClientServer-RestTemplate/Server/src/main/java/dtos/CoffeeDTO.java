package dtos;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeDTO {
    private Integer id;
    private String name;
    private String origin;
    private Integer quantity;
    private Integer price;
}
