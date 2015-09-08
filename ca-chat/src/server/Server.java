package server;

import utils.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Properties properties = Utils.initProperties("server.properties");
    private static LinkedBlockingDeque<Message> messages;
    private static LinkedBlockingDeque<ConnectionToClient> clients;

    private static ServerSocket serverSocket;
    private int port;
    private String ip;

    public Server() {
        this.port = Integer.parseInt(properties.getProperty("port"));
        this.ip = properties.getProperty("serverIp");

        clients = new LinkedBlockingDeque<>();
        messages = new LinkedBlockingDeque<>();


    }

    public void startServer() throws IOException {
        // Initialize server
        try {
            String logFile = properties.getProperty("logFile");
            Utils.setLogFile(logFile, Server.class.getName());

            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            Logger.getLogger(Server.class.getName()).log(Level.INFO, ("Server started on port " + port));

            run();
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            Utils.closeLogger(Server.class.getName());
        }
    }

    private void run() {
        // Start accepting clients
        new AcceptThread().start();
        // process incoming messages
        while (true) {
            try {
                Message message = messages.take();
                // protocol
                Logger.getLogger(Server.class.getName()).log(Level.INFO, ("Sending message: " + message.getContent()));
                if (message.equals(Message.SENDTOALL)) {
                    sendAll(message);
                } else {
                    send(message);
                }
            } catch (InterruptedException e) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    // Send to all excluding sender
    private void sendAll(Message message) {
        for (ConnectionToClient client : clients) {
            if (!client.equals(message.getFrom())) {
                client.send(message.getContent());
            }
        }
    }

    private void send(Message message) {
        for (ConnectionToClient client : message.getRecipients()) {
            client.send(message.getContent());
        }
    }

    public static LinkedBlockingDeque<Message> getMessages() {
        return messages;
    }

    public static LinkedBlockingDeque<ConnectionToClient> getClients() {
        return clients;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void addClient(ConnectionToClient client) throws InterruptedException {
        clients.put(client);
    }

}
