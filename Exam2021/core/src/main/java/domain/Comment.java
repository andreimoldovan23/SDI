package domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

@Entity
public class Comment extends BaseEntity<Long> {

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
