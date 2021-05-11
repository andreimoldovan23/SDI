package dtos;

import domain.Status;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderDTO {
    private Integer id;
    private Status status;
    private String time;
    private ClientDTO client;
    private CoffeeDTO coffee;
}
