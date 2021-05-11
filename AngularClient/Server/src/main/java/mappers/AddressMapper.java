package mappers;

import domain.Address;
import dtos.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface AddressMapper {
    Address DTOtoAddress(AddressDTO dto);
    AddressDTO addressToDTO(Address address);
}
