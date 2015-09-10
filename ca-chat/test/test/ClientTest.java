/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Utils;

/**
 *
 * @author Jonas
 */
public class ClientTest implements Observer{

    Client client;
    Thread t1;
    Properties properties = Utils.initProperties("server.properties");

    int port;
    String ip;
    String messageReceived;

    public ClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        client = new Client();
        t1 = new Thread(client);
        ip = properties.getProperty("serverIp");
        port = Integer.parseInt(properties.getProperty("port"));
        client.addObserver(this);
        client.setName("testClient");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void clientTestConnection() throws InterruptedException {
        client.connect(ip, port);
        t1.start();
        Thread.sleep((2000));//Have to wait for the response, else it would be null.
       assertEquals("USERLIST#testClient",messageReceived);

    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Override
    public void update(Observable o, Object arg) {
        messageReceived = (String)arg;
    }
}
