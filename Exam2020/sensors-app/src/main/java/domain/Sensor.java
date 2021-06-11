package domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class Sensor implements Serializable {
    private Integer id;
    private String name;
}
