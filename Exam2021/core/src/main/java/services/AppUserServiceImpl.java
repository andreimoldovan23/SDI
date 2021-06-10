package services;

import domain.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.AppUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Transactional
    @Override
    public AppUser find(Long id) {
        log.trace("Searching only user w/ id = {}", id);

        return appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find user w/ id = " + id));
    }

    @Transactional
    @Override
    public AppUser findWithFollowers(Long id) {
        log.trace("Searching user with followers w/ id = {}", id);

        AppUser user = appUserRepository.findWithFollowers(id);
        if (user == null) throw new RuntimeException("Couldn't find user with followers w/ id = " + id);
        return user;
    }

    @Transactional
    @Override
    public AppUser findWithFollowersAndPosts(Long id) {
        log.trace("Searching user with followers and posts w/ id = {}", id);

        AppUser user = appUserRepository.findWithPostsAndFollowers(id);
        if (user == null) throw new RuntimeException("Couldn't find user with followers and posts w/ id = " + id);
        return user;
    }

}
