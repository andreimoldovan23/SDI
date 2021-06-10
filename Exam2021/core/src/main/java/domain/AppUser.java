package domain;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"followers", "posts"})
@ToString(callSuper = true, exclude = {"followers", "posts"})

@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name = "graph.userWithFollowers",
                attributeNodes = @NamedAttributeNode(value = "followers", subgraph = "subgraph.followersAddress"),
                subgraphs = @NamedSubgraph(name = "subgraph.followersAddress", attributeNodes = @NamedAttributeNode("address"))),

        @NamedEntityGraph(name = "graph.userWithFollowersAndPosts",
                attributeNodes = {@NamedAttributeNode(value = "followers", subgraph = "subgraph.followersAddressPosts"),
                        @NamedAttributeNode(value = "posts", subgraph = "subgraph.postsWithComments")},
                subgraphs = {@NamedSubgraph(name = "subgraph.followersAddressPosts", attributeNodes = @NamedAttributeNode("address")),
                        @NamedSubgraph(name = "subgraph.postsWithComments", attributeNodes = @NamedAttributeNode("comments"))})
})
public class AppUser extends User {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Builder.Default
    private Set<Follower> followers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

}
