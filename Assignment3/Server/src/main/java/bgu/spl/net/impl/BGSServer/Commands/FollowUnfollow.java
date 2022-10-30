package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;


public class FollowUnfollow extends BaseCommand {

    private String username;
    private String followOrUnfollow;

    public FollowUnfollow(){
        this("", "");
    }

    public FollowUnfollow(String followOrUnfollow, String username){
        super((short)4);
        this.username = username;
        this.followOrUnfollow = followOrUnfollow;
    }

    @Override
    public Object[] commandStructure() {
        String [] structure = {username, followOrUnfollow};
        return structure;
    }

    public void act(User loggedUser,int connectionId) {
        User userToFollowUnfollow = DataBase.getInstance().getUser(username);
        if (loggedUser.getUsername() != null && !loggedUser.getUsername().equals("") && userToFollowUnfollow!=null && !userToFollowUnfollow.getBlockedUsernames().contains(loggedUser.getUsername())) {
            if (followOrUnfollow.equals("0")) {
                if(!loggedUser.getFollowingUsernames().contains(username)) {
                    loggedUser.addToFollowing(userToFollowUnfollow);
                    userToFollowUnfollow.addToFollowedByList(loggedUser);
                    if (!ConnectionsImpl.getInstance().send(connectionId,new Ack((short)4)))
                        System.out.println("Could not send  follow / unfollow acknowledge ");
                }
                else
                    if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 4)))
                        System.out.println("Could not send follow / unfollow error");
            }
            else {
                if(loggedUser.getFollowingUsernames().contains(username)) {
                    loggedUser.removeFromFollowing(userToFollowUnfollow);
                    userToFollowUnfollow.removeFromFollowedByList(loggedUser);
                    if (!ConnectionsImpl.getInstance().send(connectionId,new Ack((short)4)))
                        System.out.println("Could not send follow / unfollow acknowledge ");
                }
                else
                    if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 4)))
                        System.out.println("Could not send follow / unfollow error");
            }
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 4)))
                System.out.println("Could not send follow / unfollow error");
        }
    }
}
