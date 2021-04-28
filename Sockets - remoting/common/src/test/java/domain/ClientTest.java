package domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ClientTest {

    private Client c1;
    private Client c2;

    @BeforeEach
    public void setUp() {
        c1 = Client.Builder()
                .id(14)
                .firstName("John")
                .lastName("Mike")
                .age(19)
                .build();

        c2 = Client.Builder()
                .id(17)
                .firstName("George")
                .lastName("Jackson")
                .build();
    }

    @AfterEach
    public void tearDown() {
        c1 = null;
        c2 = null;
    }

    @Test
    void testEquals() {
        Client c3 = c1;
        assertEquals(c3, c1);
        assertNotEquals(c2, c1);
    }

    @Test
    void testToString() {
        assertEquals("Client(super=BaseEntity(id=14), firstName=John, lastName=Mike, addressId=null, age=19, phoneNumber=null)",
                c1.toString());
    }

}