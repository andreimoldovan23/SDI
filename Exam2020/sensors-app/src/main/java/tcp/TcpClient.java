package tcp;

import domain.Message;
import domain.SensorData;
import domain.SensorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import services.TcpService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class TcpClient {
    private final TcpHelper tcpHelper;

    public void run(SensorProperties properties) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(TcpService.HOST, TcpService.PORT));
        socket.configureBlocking(false);
        socket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        boolean done = false;

        while (!done) {
            selector.selectNow();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if (selectionKeys.size() == 0) {
                continue;
            }

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isReadable()) {

                    log.trace("reading message from server");
                    tcpHelper.receive((SocketChannel) key.channel());
                    log.trace("terminating program");
                    done = true;
                    break;

                } else if (key.isWritable()) {

                    sendMessage(properties, (SocketChannel) key.channel());

                }

                iterator.remove();
            }
        }

        socket.close();
    }

    private void sendMessage(SensorProperties properties, SocketChannel channel) {
        log.trace("sending message to server");
        SensorData data = getData(properties);
        if (data != null) {
            log.trace("got data: {}", data);
            Message message = new Message(Message.OK, data);
            tcpHelper.send(channel, message);
        }
    }

    private SensorData getData(SensorProperties sensorProperties) {
        log.trace("Generating measurement from: {}", sensorProperties);

        try {
            Thread.sleep(5000);
            Integer measurement = (int) Math.floor(Math.random() *
                    (sensorProperties.getUpperBound() - sensorProperties.getLowerBound() + 1) + sensorProperties.getLowerBound());
            return new SensorData(sensorProperties.getId(), sensorProperties.getName(), measurement);
        } catch (InterruptedException ie) {
            log.trace("Error while putting thread on sleep");
            return null;
        }
    }
}
