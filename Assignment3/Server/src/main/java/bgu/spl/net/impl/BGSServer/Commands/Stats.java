package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;

import java.util.LinkedList;

public class Stats extends BaseCommand {

    private String usernamesList;

    public Stats(String usernamesList) {
        super((short)8);
        this.usernamesList = usernamesList;
    }

    public Stats(){
        this("");
    }

    @Override
    public Object[] commandStructure() {
        String [] structure = {""};
        return structure;
    }

    private LinkedList<String> getUsers(){
        LinkedList<String> userlist = new LinkedList<>();
        String current = usernamesList;
        while(!current.equals("")){
            int spacer = current.indexOf('&');
            if(spacer > 0)
            {
                userlist.add(current.substring(0,spacer));
                current = current.substring(spacer + 1);
            }
            else{
                userlist.add(current);
                current = "";
            }
        }
        return userlist;
    }

    public void act(User loggedUser, int connectionId) {
        String info = "";
        LinkedList<String> users = getUsers();
        if (loggedUser != null && !loggedUser.getUsername().equals("") ){
            boolean error = false;
            for(String username : users ){
                if(!error) {
                    if (username != null && DataBase.getInstance().getUser(username) != null && !DataBase.getInstance().getUser(username).getBlockedUsernames().contains(loggedUser.getUsername())) {
                        User u = DataBase.getInstance().getUser(username);
                        info = info + " " + u.getUsername() + " " + u.getAge() + " " + u.getPostQueue().size() + " " + u.getFollowedByUsernames().size() + " " + u.getFollowingUsernames().size() + "$";
                    }
                    else {
                        if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 8)))
                            System.out.println("Could not send stat error");
                        error = true;
                    }
                }
            }
            if(info.length()>0)
                info = info.substring(0 , info.length()-1) + "#";
            if(!error)
                if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short)8, info)))
                    System.out.println("Could not send stat acknowledge");
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short)8)))
                System.out.println("Could not send stat error");
        }
    }
}
