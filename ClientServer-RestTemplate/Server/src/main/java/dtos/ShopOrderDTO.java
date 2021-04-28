package dtos;

import domain.Status;
import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShopOrderDTO {
    private Integer id;
    private Status status;
    private LocalDateTime time;
    private ClientDTO client;
    private CoffeeDTO coffee;
}
