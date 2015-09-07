package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Jonas
 */
public class Server {

    private static LinkedBlockingDeque<String> messages;
    private List<ConnectionToClient> clients;

    private ServerSocket serverSocket;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    private void startServer() throws IOException {
        serverSocket = new ServerSocket(port);
        
        while(true)
        {
        Socket socket = serverSocket.accept();
        clients.add(new ConnectionToClient(socket));
        
        }
    }

    public static LinkedBlockingDeque<String> getMessages() {
        return messages;
    }
}
