package tcp;

import domain.Message;
import services.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class TcpClient {

    public TcpClient() {}

    public Message sendAndReceive(Message request)  {
        Message response = new Message();
        try (var socket = new Socket(Service.HOST, Service.PORT);
             var is = socket.getInputStream();
             var os = socket.getOutputStream()) {

            socket.setReuseAddress(true);
            request.writeTo(os);

            response.readFrom(is);

            Optional.of(response.getHeader())
                    .filter(h -> h.equals(Message.ERROR))
                    .ifPresent(s -> System.out.println(((String) response.getBody().get(0))));

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("exception in send and receive");
        }
        return response;
    }
}
