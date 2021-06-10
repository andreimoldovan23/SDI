package services;

import domain.AppUser;

public interface AppUserService {
    AppUser find(Long id);
    AppUser findWithFollowers(Long id);
    AppUser findWithFollowersAndPosts(Long id);
}
