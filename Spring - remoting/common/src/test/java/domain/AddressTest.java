package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AddressTest {


    private Address a1;
    private Address a2;

    @BeforeEach
    public void setUp() {
        a1 = Address.Builder()
                .id(14)
                .city("a")
                .street("b")
                .number(1)
                .build();

        a2 = Address.Builder()
                .id(17)
                .city("c")
                .street("d")
                .number(2)
                .build();
    }

    @AfterEach
    public void tearDown() {
        a1 = null;
        a2 = null;
    }

    @Test
    void testEquals() {
        Address a3 = a1;
        assertEquals(a3, a1);
        assertNotEquals(a2, a1);
    }

    @Test
    void testToString() {
        assertEquals("Address(super=BaseEntity(id=14), city=a, street=b, number=1)",
                a1.toString());
    }



}
