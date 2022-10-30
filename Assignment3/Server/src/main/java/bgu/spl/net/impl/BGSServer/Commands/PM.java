package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;

import java.util.LinkedList;

public class PM extends BaseCommand {

    private String username;
    private String content;

    public PM() {
        this("", "");
    }

    public PM(String username, String content) {
        super((short)6);
        this.username = username;
        this.content = content;
    }

    @Override
    public Object[] commandStructure() {
        String [] structure = {username, content};
        return structure;
    }

    public void act(User loggedUser, int connectionId, LinkedList<String> filteredWords) {
        for(String word: filteredWords){
            content = content.replaceAll(word, "<filtered>");
        }
        if ((loggedUser.getUsername() != null && !loggedUser.getUsername().equals("")) && !DataBase.getInstance().getUser(username).getBlockedUsernames().contains(loggedUser.getUsername()) &&
                loggedUser.getFollowingUsernames().contains(username) && sendNotificationToUsername(username, new Notification((byte) 0, loggedUser.getUsername(), content))) {
            loggedUser.addToPM(this);
            if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short) 6)))
                System.out.println("Could not send pm acknowledge");
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 6)))
                System.out.println("Could not send pm error");
        }
    }

    private boolean sendNotificationToUsername(String userName, Notification notification) {
        User u = DataBase.getInstance().getUser(userName);
        if (u == null || notification == null)
            return false;
        if (!u.isLogged() || !ConnectionsImpl.getInstance().send(u.getConnectionID(), notification))
            u.getNotifications().add(notification);
        return true;
    }

}
