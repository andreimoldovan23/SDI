package domain;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SensorData extends Sensor {
    private Integer measurement;

    public SensorData(Integer id, String name, Integer measurement) {
        super(id, name);
        this.measurement = measurement;
    }
}
