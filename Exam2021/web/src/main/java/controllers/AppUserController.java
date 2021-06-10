package controllers;

import domain.AppUser;
import dtos.AppUserDTO;
import lombok.RequiredArgsConstructor;
import mappers.AppUserMapper;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/user/{id}")
    public AppUserDTO getUserDetails(@PathVariable Long id, @RequestBody AppUserDTO user) {
        AppUser appUser = mapper.dtoToEntity(user);
        if (appUser.getFollowers().size() != 0 && appUser.getPosts().size() != 0) {
            return mapper.entityPostsFollowersToDTO(appUserService.findWithFollowersAndPosts(id));
        } else if (appUser.getFollowers().size() != 0) {
            return mapper.entityFollowersToDTO(appUserService.findWithFollowers(id));
        } else {
            return mapper.entitySimpleToDTO(appUserService.find(id));
        }
    }

}
