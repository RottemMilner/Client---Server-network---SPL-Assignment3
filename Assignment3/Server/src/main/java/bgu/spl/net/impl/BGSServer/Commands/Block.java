package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;

public class Block extends BaseCommand {

    String blockedUsername;

    public Block(){
        this("");
    }

    public Block(String username) {
        super((short)12);
        this.blockedUsername = username;
    }

    @Override
    public Object[] commandStructure() {
        String [] structure = {blockedUsername};
        return structure;
    }

    public void act(User loggedUser, int connectionId){
        if (loggedUser != null && !loggedUser.getUsername().equals("") && !loggedUser.getBlockedUsernames().contains(blockedUsername)){
            loggedUser.getBlockedUsernames().add(blockedUsername);
            if(!ConnectionsImpl.getInstance().send(connectionId, new Ack((short)12)))
                System.out.println("Could not send block acknowledge");
            else {
                User blockedUser = DataBase.getInstance().getUser(blockedUsername);
                loggedUser.removeFromFollowing(blockedUser);
                loggedUser.removeFromFollowedByList(blockedUser);
                blockedUser.removeFromFollowing(loggedUser);
                blockedUser.removeFromFollowedByList(loggedUser);
            }
        }
        else{
            if(!ConnectionsImpl.getInstance().send(connectionId, new Error((short)12)))
                System.out.println("Could not send block error");
        }
    }
}
