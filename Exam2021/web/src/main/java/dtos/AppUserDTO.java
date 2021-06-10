package dtos;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDTO {
    private Long id;
    private String name;

    @Builder.Default
    private Set<FollowersDTO> followers = new HashSet<>();

    @Builder.Default
    private Set<PostsDTO> posts = new HashSet<>();
}
