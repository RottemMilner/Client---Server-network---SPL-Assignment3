package bgu.spl.net.impl.BGSServer.Commands;

import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logstat extends BaseCommand {

    public Logstat() {
        super((short)7);
    }

    @Override
    public Object[] commandStructure() {
        return new Object[0];
    }

    public void act(User loggedUser, int connectionId) {
        ConcurrentLinkedQueue<String> allUsernamesList = DataBase.getInstance().getUsernamesList();
        LinkedList<String> usernamesList = new LinkedList<>();
        for(String username : allUsernamesList){
            if(DataBase.getInstance().getUser(username).isLogged() && !DataBase.getInstance().getUser(username).getBlockedUsernames().contains(loggedUser.getUsername()))
                usernamesList.add(username);
        }
        String info = "";
        if (loggedUser != null && !loggedUser.getUsername().equals("")){
            for(String username : usernamesList){
                if(username != null && DataBase.getInstance().getUser(username) != null){
                    User u = DataBase.getInstance().getUser(username);
                    info = info + " " + u.getUsername() + " " + u.getAge() + " " + u.getPostQueue().size() + " " + u.getFollowingUsernames().size() + " " + u.getFollowedByUsernames().size() + "$";
                }
            }
            info = info.substring(0 , info.length()-1) + "#";
            if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short)7, info)))
                System.out.println("Could not send stat acknowledge");
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short)7)))
                System.out.println("Could not send stat error");
        }
    }
}
