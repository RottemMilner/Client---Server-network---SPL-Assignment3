package bgu.spl.net.impl.BGSServer.Commands;


import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;



import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post extends BaseCommand {

    private String content;

    public Post(String content) {
        super((short)5);
        this.content = content;
    }

    public Post(){
        this("");
    }

    @Override
    public Object[] commandStructure() {
        String [] structure = new String[]{content};
        return structure;
    }

    public String getContent() {
        return content;
    }

    public void act(User loggedUser, int connectionId) {
        if (loggedUser.getUsername() != null && !loggedUser.getUsername().equals("")) {
            LinkedList<String> usersToReceiveThePost = new LinkedList<>();
            loggedUser.addPost(this);
            for (String username : findTaggedUsernames(content)) {
                if ((!usersToReceiveThePost.contains(username)) && !DataBase.getInstance().getUser(username).getBlockedUsernames().contains(loggedUser.getUsername()) && (!sendNotificationToUsername(username, new Notification((byte) 1, loggedUser.getUsername(), content))))
                    System.out.println("Could not send post notification");
                usersToReceiveThePost.add(username);
            }
            ConcurrentLinkedQueue<String> followedBy = loggedUser.getFollowedByUsernames();
            for(String username : followedBy){
                if ((!usersToReceiveThePost.contains(username)) && (!sendNotificationToUsername(username, new Notification((byte) 1, loggedUser.getUsername(), content))))
                    System.out.println("Could not send post notification");
                usersToReceiveThePost.add(username);
            }
            if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short)5)))
                System.out.println("Could not send post acknowledge");
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short)5)))
                System.out.println("Could not send post error");
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

    private String[] findTaggedUsernames(String content) {
        String [] s = new String[0];
        if (content != null && !content.equals("")) {
            Pattern p = Pattern.compile("@(.+?) ");
            Matcher m = p.matcher(content);
            LinkedList<String> usernamesList = new LinkedList<>();
            while (m.find())
                usernamesList.add(m.group().substring(1, m.group().length() - 1));
            if (content.lastIndexOf("@") > content.lastIndexOf(" ") && !(content.substring(content.lastIndexOf("@"))).substring(1).isEmpty())
                usernamesList.add((content.substring(content.lastIndexOf("@"))).substring(1));
            s = usernamesList.toArray(new String[0]);
        }
        if (s != null) {
            LinkedList<String> validUsernames = new LinkedList<>();
            for (String u : s)
                if (DataBase.getInstance().getUser(u) != null)
                    validUsernames.add(u);
            return validUsernames.toArray(new String[0]);
        }
        return new String[0];
    }
}