package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
            ChatProtocol protocol = new ChatProtocol(this);
            //Waiting for client input
            while ((input = in.readLine()) != null && running) {
                Logger.getLogger(ConnectionToClient.class.getName()).log(Level.INFO, ("Received message: " + input));
                Message message = protocol.processInput(input);
                Server.getMessages().put(message); //Putting client input into a LinkedBlockingDeque
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            removeClient();
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
        Server.getClients().remove(this);
        running = false;
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
