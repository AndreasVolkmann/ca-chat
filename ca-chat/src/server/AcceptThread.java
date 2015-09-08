package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Av on 08-09-2015.
 */
public class AcceptThread extends Thread {

    private ServerSocket serverSocket;

    public AcceptThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Server.getClients().put(new ConnectionToClient(socket));
                Logger.getLogger(AcceptThread.class.getName()).log(Level.INFO, ("New client connected on port " + socket.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
