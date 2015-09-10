package client;

import java.time.LocalDateTime;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonas
 */
public class ClientGUILittleHelper {

    

    public void getAndSetClientName(Client client) {
        client.setName(JOptionPane.showInputDialog("Welcome to the chat! Before we start, what's your name?"));
        while (client.getName().length() < 1) {
            client.setName(JOptionPane.showInputDialog("You need a name!"));
            if (client.getName() == null) {
                System.exit(-1);
            }
        }
        if (client.getName() == null) {
            System.exit(-1);
        }
    }

    public Object understandMessage(Object arg) {
        String message = (String) arg;
        String messageArray[] = message.split("#");
        if (messageArray[0].equals("MSG")) {
            
            message =  getHourMinute()+ " | " + messageArray[1] + " : " + messageArray[2];
            return message;
        } else if (messageArray[0].equals("USERLIST")) {
            String[] names = message.substring(message.indexOf("#") + 1, message.length()).split(",");
            return names;
        } else {
            //message = "Unknown Command";
            return message;
        }
    }

    public String formatSend(JList jList1) {
        String message = "MSG#";
        int i = 1;
        if (jList1.getSelectedValuesList().isEmpty()) {
            message += "*";
        } else {
            for (Object col : jList1.getSelectedValuesList()) {
                if (jList1.getSelectedValuesList().size() == 1) {
                    message += col;
                } else {
                    if (i == jList1.getSelectedValuesList().size()) {
                        message += col;
                    } else {
                        message += col;
                        message += ",";
                    }

                }
                i++;
            }
        }
        return message;
    }
    
    public String getHourMinute()
    {
        LocalDateTime datetime = LocalDateTime.now();
        return datetime.getHour() + ":" + datetime.getMinute();
    }
}
