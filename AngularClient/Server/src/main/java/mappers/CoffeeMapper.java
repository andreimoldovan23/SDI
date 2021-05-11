package mappers;

import domain.Coffee;
import dtos.CoffeeDTO;
import org.mapstruct.Mapper;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface CoffeeMapper {
    Coffee DTOtoCoffee(CoffeeDTO coffeeDTO);
    CoffeeDTO coffeeToDTO(Coffee coffee);
}
