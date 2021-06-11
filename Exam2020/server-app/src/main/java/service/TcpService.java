package service;

import domain.Message;
import domain.SensorData;
import domain.SensorInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import repo.SensorRepo;
import tcp.TcpHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class TcpService {

    private final SensorRepo sensorRepo;
    private final TcpHelper tcpHelper;
    private final Set<String> toKill = new HashSet<>();

    public void startServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel socket = ServerSocketChannel.open();
        socket.bind(new InetSocketAddress(services.TcpService.HOST, services.TcpService.PORT));
        socket.configureBlocking(false);
        socket.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select(50000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            if (selectionKeys.size() == 0)
                break;
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {

                    log.trace("accepted connection");
                    register(selector, socket.accept());

                } else if (key.isReadable()) {

                    log.trace("proceeding to read from socket");
                    Message message = tcpHelper.receive((SocketChannel) key.channel());
                    if (message != null) {

                        if (message.getHeader().equals(Message.KILL)) {

                            toKill.add((String) message.getBody());
                            key.channel().close();
                            key.cancel();

                        } else {

                            String sensorName = ((SensorData) message.getBody()).getName();
                            if (toKill.contains(sensorName)) {
                                tcpHelper.send((SocketChannel) key.channel(), new Message(Message.KILL, null));
                                key.channel().close();
                                key.cancel();
                            }
                            else
                                insert((SensorData) message.getBody());

                        }

                    }

                }

                iterator.remove();
            }
        }
        log.trace("terminating server");
        socket.close();
    }

    private void register(Selector selector, SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void insert(SensorData data) {
        SensorInfo sensorInfo = new SensorInfo(data.getName(), data.getMeasurement());
        sensorRepo.insert(sensorInfo);
    }

}
