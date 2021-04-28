package tcp;

import domain.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.UnaryOperator;

public class TcpServer {
    private final ExecutorService executorService;
    private final Map<String, UnaryOperator<Message>> methodHandlers;
    private final int port;


    public TcpServer(ExecutorService executorService, int port) {
        this.executorService = executorService;
        this.methodHandlers = new HashMap<>();
        this.port = port;
    }

    public void startServer() {
        try (var serverSocket = new ServerSocket(this.port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("server started; waiting for clients...");
            while(true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setReuseAddress(true);
                    System.out.println("client connected");
                    executorService.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    System.out.println("Error accepting connection");
                }
            }
        } catch (IOException e) {
            System.out.println("Starting Error: " + e.getMessage());
        }
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler) {
        methodHandlers.put(methodName, handler);
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;

        private ClientHandler(Socket clientSocket) {
            this.socket = clientSocket;
        }

        @Override
        public void run() {
            try (var is = socket.getInputStream();
                 var os = socket.getOutputStream()) {

                Message request = new Message();
                request.readFrom(is);
                System.out.println("received request: " + request);

                Message response;

                response = methodHandlers.get(request.getHeader()).apply(request);
                System.out.println("computed response: " + response);

                response.writeTo(os);
                System.out.println("response sent to client");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
