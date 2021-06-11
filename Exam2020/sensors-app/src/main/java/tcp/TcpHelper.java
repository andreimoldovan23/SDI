package tcp;

import domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Component
@Slf4j
public class TcpHelper {
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

    public Message receive(SocketChannel socket) {
        byteBuffer.clear();

        try {
            socket.read(byteBuffer);
            byteBuffer.flip();
            Message message = (Message) TcpHelper.deserialize(byteBuffer.array());

            log.trace("Received message: {}", message);
            return message;
        } catch (IOException ioe) {
            log.trace("Error while reading from socket");
        } catch (ClassNotFoundException cle) {
            log.trace("Error while deserializing message");
        }
        return null;
    }

    public void send(SocketChannel socket, Message message) {
        byteBuffer.clear();

        try {
            log.trace("Sending message: {}", message);
            byteBuffer.put(TcpHelper.serialize(message));
            byteBuffer.flip();
            while (byteBuffer.hasRemaining())
                socket.write(byteBuffer);
        } catch (IOException ioe) {
            log.trace("Error while writing to socket");
        }
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        os.flush();
        return out.toByteArray();
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
