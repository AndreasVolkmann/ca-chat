/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

/**
 *
 * @author Jonas
 */
public class Client extends Observable implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public void connect(String address, int port) {
        try {

            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            this.socket.setSoTimeout(0);
            send("USER#" + name);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void send(String msg) {
        out.println(msg);
    }

    public void printToOwnClient(String message) {
        setChanged();
        notifyObservers(message);
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Recieved: " + message);
               
                setChanged();
                notifyObservers(message);

            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        Thread t1 = new Thread(client);
        Properties properties = Utils.initProperties("server.properties");

        int port;
        String ip;
        ip = properties.getProperty("serverIp");
        port = Integer.parseInt(properties.getProperty("port"));
        client.setName("Client");
        client.connect(ip, port);
        t1.start();

    }

}
