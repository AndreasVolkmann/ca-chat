package server;

import java.util.ArrayList;

/**
 * Created by Av on 08-09-2015.
 */
public class Message {

    public static final Message SENDTOALL = new Message();
    public static final Message ERROR = new Message("ERROR");

    private ConnectionToClient from;
    private ArrayList<ConnectionToClient> to = new ArrayList<>();
    private String content;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }


    public ConnectionToClient getFrom() {
        return from;
    }

    public ArrayList<ConnectionToClient> getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public void setFrom(ConnectionToClient from) {
        this.from = from;
    }

    public void setTo(ArrayList<ConnectionToClient> to) {
        this.to = to;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
