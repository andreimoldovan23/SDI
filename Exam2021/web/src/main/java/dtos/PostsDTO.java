package dtos;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostsDTO {
    private Long id;
    private String title;

    @Builder.Default
    private Set<CommentsDTO> comments = new HashSet<>();
}
