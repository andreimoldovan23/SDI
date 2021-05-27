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
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

@Entity
public class ShopOrder extends BaseEntity<Integer> {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "coffee_id")
    private Coffee coffee;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime time;

}
