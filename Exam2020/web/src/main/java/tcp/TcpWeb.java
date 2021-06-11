package tcp;

import domain.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import services.TcpService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Component
@RequiredArgsConstructor
@Slf4j
public class TcpWeb {
    private final TcpHelper tcpHelper;

    public void kill(String name) throws IOException {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(TcpService.HOST, TcpService.PORT));

        log.trace("sending kill message to server for: {}", name);
        Message message = new Message(Message.KILL, name);
        tcpHelper.send(socket, message);

        socket.close();
    }
}
