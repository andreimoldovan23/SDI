package domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * ShopOrder Entity
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(exclude = {"coffee", "client"}, callSuper = true)
@EqualsAndHashCode(exclude = {"status", "time", "coffee", "client"}, callSuper = true)

@Entity
public class ShopOrder extends BaseEntity<Integer> {

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "coffee_id")
    private Coffee coffee;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime time;

    @Builder(builderMethodName = "Builder")
    private ShopOrder(Integer id, Client client, Coffee coffee, Status status, LocalDateTime time) {
        super(id);
        this.status = status;
        this.time = time;
        this.client = client;
        this.coffee = coffee;
    }

}
