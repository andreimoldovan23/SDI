package dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowersDTO {
    private Long id;
    private String name;
    private AddressDTO address;
}
