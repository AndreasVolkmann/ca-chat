package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Jonas
 */
public class Server {

    private static LinkedBlockingDeque<String> messages;
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
                String message = messages.take();
                // protocol
                sendAll(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    private void sendAll(String message) {
        for (ConnectionToClient client : clients) {
            client.send(message);
        }
    }

    public static LinkedBlockingDeque<String> getMessages() {
        return messages;
    }

    public static LinkedBlockingDeque<ConnectionToClient> getClients() {
        return clients;
    }

}
