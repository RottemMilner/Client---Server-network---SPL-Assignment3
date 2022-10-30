package bgu.spl.net.impl.BGSServer.Commands;

import bgu.spl.net.impl.BGSServer.BaseCommand;
import bgu.spl.net.impl.BGSServer.ConnectionsImpl;
import bgu.spl.net.impl.BGSServer.DataBase;
import bgu.spl.net.impl.BGSServer.User;


public class Login extends BaseCommand {

    private String username;
    private String password;
    private User loggedUser;
    private String captcha;

    public Login(String username, String password, String captcha){
        super((short)2);
        this.username = username;
        this.password = password;
        this.loggedUser = DataBase.getInstance().getUser(username);
        this.captcha = captcha;
    }

    public Login(){
        this("", "","" );
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser(){
        return loggedUser;
    }

    @Override
    public Object[] commandStructure() {
        Object [] commandStructure =  {username, password , captcha};
        return commandStructure;
    }

    public boolean act(String loggedUsername, int connectionId){
        User u = DataBase.getInstance().getUser(username);
        if (captcha.equals("1") && u != null && u.getPassword().equals(password) && loggedUsername.equals("")&& u.login(connectionId)){
            if (!ConnectionsImpl.getInstance().send(connectionId, new Ack((short)2)))
                System.out.println("Could not send acknowledge");
            Notification nextNotification = u.getNotifications().poll();
            while (nextNotification != null) {
                if (!ConnectionsImpl.getInstance().send(connectionId, nextNotification))
                    System.out.println("Could not send awaiting notification");
                nextNotification = u.getNotifications().poll();
            }
            return true;
        }
        else {
            if (!ConnectionsImpl.getInstance().send(connectionId, new Error((short) 2)))
                System.out.println("Could not send Error");
            return false;
        }
    }
}
