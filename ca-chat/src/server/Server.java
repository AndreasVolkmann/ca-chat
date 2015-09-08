package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Jonas
 */
public class Server {

    private static LinkedBlockingDeque<Message> messages;
    private static LinkedBlockingDeque<ConnectionToClient> clients;

    private ServerSocket serverSocket;
    private int port;

    public Server(int port) {
        this.port = port;

        clients = new LinkedBlockingDeque<>();
        messages = new LinkedBlockingDeque<>();


    }

    public void startServer() throws IOException {

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new AcceptThread(serverSocket).start();

        while (true) {
            try {
                Message message = messages.take();
                // protocol
                if (message.equals(Message.SENDTOALL)) {
                    sendAll(message);
                } else {
                    send(message);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private void sendAll(Message message) {
        for (ConnectionToClient client : clients) {
            client.send(message.getContent());
        }
    }

    private void send(Message message) {
        for (ConnectionToClient client : message.getTo()) {
            client.send(message.getContent());
        }
    }

    public static LinkedBlockingDeque<Message> getMessages() {
        return messages;
    }

    public static LinkedBlockingDeque<ConnectionToClient> getClients() {
        return clients;
    }

}
