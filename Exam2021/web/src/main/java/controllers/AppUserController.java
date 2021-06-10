package controllers;

import dtos.AppUserDTO;
import lombok.RequiredArgsConstructor;
import mappers.AppUserMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import services.AppUserService;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserMapper mapper;

    @PostMapping("/user-hello/{id}")
    public AppUserDTO getUser(@PathVariable Long id) {
        return mapper.entitySimpleToDTO(appUserService.find(id));
    }

    @GetMapping("/user-followers/{id}")
    public AppUserDTO getUserFollowers(@PathVariable Long id) {
        return mapper.entityFollowersToDTO(appUserService.findWithFollowers(id));
    }

    @GetMapping("/user-posts-followers/{id}")
    public AppUserDTO getUserPostsFollowers(@PathVariable Long id) {
        return mapper.entityPostsFollowersToDTO(appUserService.findWithFollowersAndPosts(id));
    }

}
