package domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Message implements Serializable {
    public static final String OK = "ok";
    public static final String KILL = "kill";

    private String header;
    private Object body;
}
