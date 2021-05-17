package mappers;

import domain.Coffee;
import dtos.CoffeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface CoffeeMapper {
    @Mapping(target = "orders", ignore = true)
    Coffee DTOtoCoffee(CoffeeDTO coffeeDTO);
    CoffeeDTO coffeeToDTO(Coffee coffee);
}
