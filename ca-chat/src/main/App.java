/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import server.Server;

import java.io.IOException;

/**
 *
 * @author Jonas
 */
public class App {
    
    public static void main(String[] args) throws IOException {
        Server server = new Server(9090);
        server.startServer();
    }
    
}
