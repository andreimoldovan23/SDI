package domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SensorInfo {
    private String name;
    private Integer measurement;
    private LocalDateTime time;

    public SensorInfo(String name, Integer measurement) {
        this.name = name;
        this.measurement = measurement;
        this.time = LocalDateTime.now();
    }
}
