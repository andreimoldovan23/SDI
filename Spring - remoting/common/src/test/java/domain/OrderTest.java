package domain;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OrderTest {

    private Order o1;
    private Order o2;

    @BeforeEach
    public void setUp() {
        o1 = Order.Builder()
                .id(new ImmutablePair<>(10, 12))
                .status(Status.pending)
                .time(LocalDateTime.now())
                .build();

        o2 = Order.Builder()
                .id(new ImmutablePair<>(20, 22))
                .status(Status.canceled)
                .build();
    }

    @AfterEach
    public void tearDown() {
        o1 = null;
        o2 = null;
    }

    @Test
    public void testEquals() {
        Order o3 = o1;
        assertEquals(o3, o1);
        assertNotEquals(o2, o1);
    }

    @Test
    public void testToString() {
        assertEquals("Order(super=BaseEntity(id=(20,22)), coffeesId=[], status=canceled, time=null)",
                o2.toString());
    }
}
