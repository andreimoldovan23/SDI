package mappers;

import domain.AppUser;
import dtos.AppUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface AppUserMapper {

    @Mapping(source = "followers", target = "followers", ignore = true)
    @Mapping(source = "posts", target = "posts", ignore = true)
    AppUserDTO entitySimpleToDTO(AppUser appUser);

    @Mapping(source = "posts", target = "posts", ignore = true)
    AppUserDTO entityFollowersToDTO(AppUser appUser);

    AppUserDTO entityPostsFollowersToDTO(AppUser appUser);

    AppUser dtoToEntity(AppUserDTO userDTO);
}
