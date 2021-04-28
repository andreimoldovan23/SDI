package mappers;

import domain.ShopOrder;
import dtos.ShopOrderDTO;
import org.mapstruct.Mapper;

@Mapper(config = NonBuilderConfigMapper.class, componentModel = "spring")
public interface ShopOrderMapper {
    ShopOrder DTOtoShopOrder(ShopOrderDTO dto);
    ShopOrderDTO shopOrderToDTO(ShopOrder order);
}
