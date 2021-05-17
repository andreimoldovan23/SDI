package mappers;

import domain.Address;
import dtos.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "clients", ignore = true)
    Address DTOtoAddress(AddressDTO dto);
    AddressDTO addressToDTO(Address address);
}
