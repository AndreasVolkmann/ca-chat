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

    private String clientName;

    public ConnectionToClient(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.start();
    }
    
    
    
    public void send(String message)
    {
       out.println(message);
    }
    
    public String getClientName()
    {
        return clientName;
    }
    
    public void setClientName(String Name)
    {
       this.clientName = Name;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try {
                String input = in.readLine(); //Waiting for client input
                Server.getMessages().put(input); //Putting client input into a LinkedBlockingDeque
                
            } catch (IOException ex) {
                Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionToClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        

}
