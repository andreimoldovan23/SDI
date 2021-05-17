package domain;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@ToString
@EqualsAndHashCode
@Embeddable
public class ClientInfo {
    private Integer age;
    private String phoneNumber;
}
