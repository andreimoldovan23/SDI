package mappers;

import domain.ShopOrder;
import dtos.ShopOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface ShopOrderMapper extends DateMapper {
    @Mapping(target = "time", source = "time", qualifiedByName = "stringToDate")
    ShopOrder DTOtoShopOrder(ShopOrderDTO dto);

    @Mapping(target = "time", source = "time", qualifiedByName = "dateToString")
    ShopOrderDTO shopOrderToDTO(ShopOrder order);
}
