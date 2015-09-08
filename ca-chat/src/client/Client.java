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
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonas
 */
public class Client extends Observable implements Runnable {

    private Socket socket;
    private int port;
    private String ip;
    private BufferedReader in;
    private PrintWriter out;

    public void connect(String address, int port) {
        try {
            this.port = port;
            socket = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            send("USER#Jonas");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void send(String msg) {
        out.println(msg);
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
                String messageArray[] = message.split("#");
                if (messageArray[0].equals("MSG")) {
                    LocalDateTime datetime = LocalDateTime.now();
                    message = datetime.getHour() + ":" + datetime.getMinute() + " | " + messageArray[1] + " : " + messageArray[2];
                    setChanged();
                    notifyObservers(message);
                } else if (messageArray[0].equals("USERLIST")) {
                    String[] names = message.substring(message.indexOf("#")+1, message.length()).split(",");
                    setChanged();
                    notifyObservers(names);
                } else {
                    message = "Unknown Command";
                     setChanged();
                    notifyObservers(message);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
