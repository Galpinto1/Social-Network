package bgu.spl.net.impl.BGS;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ConnectionsImpl<T> implements Connections<T> {

    //fields
    ConcurrentHashMap<Integer, ConnectionHandler> connectionHandlers;
    AtomicInteger idCounter;

    private static class ConnectionsHolder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }
    //private constructor - singleton impl
    private ConnectionsImpl(){
        connectionHandlers = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger(1);
    }

    public boolean send(int connectionId, T msg){
        ConnectionHandler ch = connectionHandlers.get(connectionId);
        if(ch == null)
            return false;
        else {
            synchronized (ch) {
                ch.send(msg);
                return true;
            }
        }
    }

    public void broadcast(T msg){
        for(ConnectionHandler<T> ch: connectionHandlers.values()){
            ch.send(msg);
        }
    }

    public void disconnect(int connectionId){
        ConnectionHandler ch = connectionHandlers.get(connectionId);
        if(ch != null) {
            connectionHandlers.remove(connectionId);
        }
    }

    public static ConnectionsImpl getInstance(){
        return ConnectionsHolder.instance;
    }

    public void addHandler(ConnectionHandler ch){
        connectionHandlers.putIfAbsent(idCounter.get(), ch);
        ch.getProtocol().start(idCounter.get(),this);
        idCounter.incrementAndGet();

    }
}
