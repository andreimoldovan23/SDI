package mappers;

import domain.Coffee;
import dtos.CoffeeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoffeeMapperTest extends AbstractMapperTest {

    @Autowired
    private CoffeeMapper coffeeMapper;

    @Test
    public void coffeeToDto() {
        coffee.setId(coffeeId);
        CoffeeDTO dto = coffeeMapper.coffeeToDTO(coffee);

        assertEquals(dto.getId(), coffeeId);
        assertEquals(dto.getName(), coffeeName);
        assertEquals(dto.getOrigin(), coffeeOrigin);
        assertEquals(dto.getQuantity(), coffeeQuantity);
        assertEquals(dto.getPrice(), coffeePrice);
    }

    @Test
    public void dtoToCoffee() {
        Coffee entity = coffeeMapper.DTOtoCoffee(coffeeDto);

        assertEquals(entity.getId(), coffeeId);
        assertEquals(entity.getName(), coffeeName);
        assertEquals(entity.getOrigin(), coffeeOrigin);
        assertEquals(entity.getQuantity(), coffeeQuantity);
        assertEquals(entity.getPrice(), coffeePrice);
    }
}
