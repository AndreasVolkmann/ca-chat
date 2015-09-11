package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonas
 */
public class ConnectionToClient extends Thread{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private boolean running;
    private String clientName;
    private ChatProtocol protocol;
    //private int id;

    public ConnectionToClient(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        running = true;
        this.start();
    }



    @Override
    public void run() {
        try {
            String input;
            protocol = new ChatProtocol(this);
            while (running && ((input = in.readLine()) != null)) {
                //Waiting for client input
                Message message = protocol.processInput(input);
                Server.getMessages().put(message); //Putting client input into a LinkedBlockingDeque
                Logger.getLogger(Server.class.getName()).log(Level.INFO, (clientName + ": " + input));
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (running) { // if it is still running it might not have been closed properly
                removeClient();
                try {
                    Server.getMessages().put(protocol.sendUserList());
                } catch (InterruptedException e) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            deconstruct();
        }
    }

    public void send(String message) {
        out.println(message);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String Name) {
        this.clientName = Name;
    }

    protected void removeClient() {
        System.out.println("Removing Client ...");
        Server.getClients().remove(this);
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        running = false;
    }

    public void setRunning(boolean condition) {
        running = condition;
    }

    private void deconstruct() {
        System.out.println("Deconstructing client ...");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }

}
