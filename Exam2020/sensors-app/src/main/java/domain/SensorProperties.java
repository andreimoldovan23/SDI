package domain;

import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SensorProperties extends Sensor {
    private Integer lowerBound;
    private Integer upperBound;

    public SensorProperties(Integer id, String name, Integer lowerBound, Integer upperBound) {
        super(id, name);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
}
