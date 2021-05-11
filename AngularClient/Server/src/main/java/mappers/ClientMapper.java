package mappers;

import domain.Client;
import dtos.ClientDTO;
import org.mapstruct.Mapper;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface ClientMapper {
    Client DTOtoClient(ClientDTO clientDTO);
    ClientDTO clientToDTO(Client client);
}
