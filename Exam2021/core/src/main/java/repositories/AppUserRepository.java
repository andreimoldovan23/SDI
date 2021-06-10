package repositories;

import domain.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends BlogRepository<AppUser, Long> {

    @Query("select u from AppUser u where u.id = ?1")
    @EntityGraph(value = "graph.userWithFollowers", type = EntityGraph.EntityGraphType.LOAD)
    AppUser findWithFollowers(Long id);

    @Query("select u from AppUser u where u.id = ?1")
    @EntityGraph(value = "graph.userWithFollowersAndPosts", type = EntityGraph.EntityGraphType.LOAD)
    AppUser findWithPostsAndFollowers(Long id);

}
