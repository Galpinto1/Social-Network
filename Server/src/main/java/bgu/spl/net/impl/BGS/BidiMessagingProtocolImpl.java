package bgu.spl.net.impl.BGS;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.impl.BGS.Message.Message;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    //fields
    private boolean shouldTerminate;
    private DataBase dataBase;
    private User my_user;
    private int connectionId;
    private Connections<Message> connections;

    //constructor
    public BidiMessagingProtocolImpl(){
        shouldTerminate = false;
        dataBase = DataBase.getInstance();
        my_user = null;
        connectionId = -1;
        connections = ConnectionsImpl.getInstance();

    }

    public void start(int connectionId, Connections<Message> connections){
        this.connectionId = connectionId;
        this.connections = connections;
    }

    public void process(Message message){
        message.process(this);
    }

    public boolean shouldTerminate(){
        return shouldTerminate;
    }

    public DataBase getDataBase(){
        return dataBase;
    }

    public User getUser(){
        return my_user;
    }

    public void setUser(User user){
        my_user = user;
    }

    public Connections getConnections(){
        return connections;
    }

    public int getConnectionId(){
        return connectionId;
    }
}
