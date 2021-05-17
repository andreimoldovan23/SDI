package mappers;

import domain.Client;
import dtos.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "orders", ignore = true)
    Client DTOtoClient(ClientDTO clientDTO);
    ClientDTO clientToDTO(Client client);
}
