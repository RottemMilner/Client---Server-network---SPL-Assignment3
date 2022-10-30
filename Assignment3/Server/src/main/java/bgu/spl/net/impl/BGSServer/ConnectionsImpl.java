package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {


    //hash map of connection handlers - one for each client

    private ConcurrentHashMap<Integer , ConnectionHandler<T>> connectionHandlers; //connectionID to connectionHandler map

    public ConnectionsImpl(){
        connectionHandlers = new ConcurrentHashMap<>();
    }

    private static class ConnectionsImplHolder{
        private static volatile ConnectionsImpl instance = new ConnectionsImpl();
    }

    public static ConnectionsImpl getInstance() {
        return ConnectionsImpl.ConnectionsImplHolder.instance;
    }

    @Override
    public boolean send(int connectionId, T message) {
        ConnectionHandler<T> ch = connectionHandlers.get(connectionId);
        if(ch!=null) {
            ch.send(message);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T message) {
        for (ConnectionHandler<T> c: connectionHandlers.values()) {
            c.send(message);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connectionHandlers.remove(connectionId);
    }

    public void connect(int connectionID, ConnectionHandler<T> ch){
        connectionHandlers.putIfAbsent(connectionID, ch);
    }
}
