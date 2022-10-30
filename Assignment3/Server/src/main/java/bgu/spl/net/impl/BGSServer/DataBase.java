package bgu.spl.net.impl.BGSServer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {

    private ConcurrentHashMap<String, User> usersMap;
    private ConcurrentLinkedQueue<String> usernamesList;

    private DataBase() {
        usersMap = new ConcurrentHashMap<>();
        usernamesList = new ConcurrentLinkedQueue<>();
    }


    private static class DatabaseHolder{
        private static volatile DataBase instance = new DataBase();
    }

    public static DataBase getInstance() {
        return DataBase.DatabaseHolder.instance;
    }

    public synchronized boolean register(User user){
        if (user != null)
                if (getUser(user.getUsername()) == null) {
                    usernamesList.add(user.getUsername());
                    if(usersMap.put(user.getUsername(), user) == null)
                        return true;
                    else
                        return false;
                }
        return false;
    }

    public User getUser(String username){
        return usersMap.get(username);
    }

    public ConcurrentLinkedQueue<String> getUsernamesList() {
        return usernamesList;
    }
}
