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
    Client client2;
    Thread t1;
    Thread t2;
    Properties properties = Utils.initProperties("server.properties");

    int port;
    String ip;
    String messageReceived;

    public ClientTest() {
        client = new Client();
        client2 = new Client();
        t1 = new Thread(client);
        t2 = new Thread(client2);
        ip = properties.getProperty("serverIp");
        port = Integer.parseInt(properties.getProperty("port"));
        client.addObserver(this);
        client2.addObserver(this);
        client.setName("testClient");
        client2.setName("testClient2");
        client.connect(ip, port);
        client2.connect(ip, port);
        t1.start();
        t2.start();
    }

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
        
    }

    @Before
    public void setUp() {
        
    }

    @After
    public void tearDown() {
        client.send("STOP#");
        client2.send("STOP#");
    }

    @Test
    public void clientTestConnection() throws InterruptedException {
        System.out.println("In clientTestConnection");
        Thread.sleep(2000);//Have to wait for the response, else it would be null.
       assertEquals("USERLIST#testClient,testClient2",messageReceived);
    }
    
    @Test
    public void clientTestSend() throws InterruptedException
    {
        System.out.println("In clientTestSend");
        Thread.sleep(2000);
        client.send("MSG#testClient2#TestingSendProtocol");
        Thread.sleep(2000);
        assertEquals("MSG#testClient#TestingSendProtocol",messageReceived);  
    }
    
    @Test
    public void clientTestStop() throws InterruptedException
    {
        System.out.println("In clientTestStop");
        Thread.sleep(2000);
        client.send("STOP#");
        Thread.sleep(2000);
        assertEquals(true,client.isConnected());
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("messageReceived = "+(String)arg);
        messageReceived = (String)arg;
    }
}
