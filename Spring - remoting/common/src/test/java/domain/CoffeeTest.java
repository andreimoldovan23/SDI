package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CoffeeTest {

    private Coffee c1;
    private Coffee c2;

    @BeforeEach
    public void setUp() {
        c1 = Coffee.Builder()
                .id(24)
                .name("Arabica")
                .origin("CentralAmerica")
                .quantity(200)
                .price(50)
                .build();

        c2 = Coffee.Builder()
                .id(30)
                .name("Robusta")
                .origin("SouthAmerica")
                .build();
    }

    @AfterEach
    public void tearDown() {
        c1 = null;
        c2 = null;
    }

    @Test
    public void testEquals() {
        Coffee c3 = c1;
        assertEquals(c3, c1);
        assertNotEquals(c2, c1);
    }

    @Test
    public void testToString() {
        assertEquals("Coffee(super=BaseEntity(id=24), name=Arabica, origin=CentralAmerica, quantity=200, price=50)",
                c1.toString());
    }
}
