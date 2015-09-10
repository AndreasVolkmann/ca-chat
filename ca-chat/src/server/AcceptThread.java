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

    public AcceptThread() {
    }

    public void run() {

        while (true) {
            try {
                Socket socket = Server.getServerSocket().accept();
                socket.setSoTimeout(0);
                socket.setKeepAlive(true);
                Server.addClient(new ConnectionToClient(socket));
                Logger.getLogger(Server.class.getName()).log(Level.INFO, ("New client connected on port " + socket.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
