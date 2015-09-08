
package main;

import server.Server;

import java.io.IOException;

public class App {
    
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }
    
}
