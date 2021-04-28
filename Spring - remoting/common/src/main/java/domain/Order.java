package domain;

import lombok.*;
import org.apache.commons.lang3.tuple.Pair;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(exclude = {"status", "time"}, callSuper = true)
public class Order extends BaseEntity<Pair<Integer, Integer>> {

    private List<Integer> coffeesId = new ArrayList<>();
    private Status status = null;
    private LocalDateTime time = null;

    @Builder(builderMethodName = "Builder")
    private Order(Pair<Integer, Integer> id, Status status, LocalDateTime time) {
        super(id);
        this.status = status;
        this.time = time;
    }

}
