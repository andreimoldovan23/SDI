package domain;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @TempDir
    static Path tempDir;

    private Message message;
    private Client client;
    private Order order;

    @BeforeEach
    public void setUp() {
        client = Client.Builder()
                .id(20)
                .firstName("Jack")
                .lastName("Wayne")
                .addressId(20)
                .age(70)
                .phoneNumber("0000000000")
                .build();

        order = domain.Order.Builder()
                .id(new ImmutablePair<>(10, 10))
                .status(Status.delivered)
                .time(LocalDateTime.now())
                .build();
        order.getCoffeesId().add(10);
        order.getCoffeesId().add(20);

        message = new Message();
        message.setHeader("Add");
        message.getBody().add(client);
        message.getBody().add(order);
    }

    @AfterEach
    public void tearDown() {
        client = null;
        message = null;
    }

    @Test
    public void readWriteTest() throws IOException {
        Message newMessage = new Message();
        Path tempFile = Files.createFile(tempDir.resolve("message.txt"));
        try(FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile().getAbsolutePath(), false);
            FileInputStream fileInputStream = new FileInputStream(tempFile.toFile().getAbsolutePath())) {
            message.writeTo(fileOutputStream);
            newMessage.readFrom(fileInputStream);
            assertEquals("Add", newMessage.getHeader());
            assertEquals(2, newMessage.getBody().size());
            assertTrue(newMessage.getBody().contains(client));
            assertTrue(newMessage.getBody().contains(order));
        } catch (ClassNotFoundException e) {
            fail();
        }
    }

}
