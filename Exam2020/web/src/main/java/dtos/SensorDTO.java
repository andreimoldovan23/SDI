package dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SensorDTO {
    private Integer id;
    private String name;
    private Integer measurement;
    private String time;
}
