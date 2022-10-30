package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BGSServer.Commands.Notification;
import bgu.spl.net.impl.BGSServer.Commands.PM;
import bgu.spl.net.impl.BGSServer.Commands.Post;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

    private String username;
    private String password;
    private String birthday;
    private boolean isLogged;
    private ConcurrentLinkedQueue<String> followingUsernames;
    private ConcurrentLinkedQueue<String> followedByUsernames;
    private ConcurrentLinkedQueue<Post> postQueue;
    private ConcurrentLinkedQueue<PM> PmQueue;
    private ConcurrentLinkedQueue<String> BlockedUsernames;
    private ConcurrentLinkedQueue<Notification> notifications;
    private Integer connectionID;


    public User(){
        this("","","");
    }

    public User(String username, String password, String birthday){
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.isLogged = false;
        this.followingUsernames = new ConcurrentLinkedQueue<String>();
        this.followedByUsernames = new ConcurrentLinkedQueue<String>();
        this.postQueue = new ConcurrentLinkedQueue<Post>();
        this.BlockedUsernames = new ConcurrentLinkedQueue<String>();
        this.PmQueue = new ConcurrentLinkedQueue<PM>();
        this.notifications = new ConcurrentLinkedQueue<Notification>();
        this.connectionID = -1;
    }

    public Integer getConnectionID() {
        return connectionID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void addToFollowing(User user){
        if (username != null && !username.equals("") && !followingUsernames.contains(user.getUsername())) {
            followingUsernames.add(user.getUsername());
        }
    }

    public void removeFromFollowing(User user){
        followingUsernames.remove(user.getUsername());
    }

    public void addToFollowedByList(User user) {
        if (username != null && !username.isEmpty() && !followedByUsernames.contains(username)) {
            followedByUsernames.add(user.getUsername());
        }
    }

    public void removeFromFollowedByList(User user){
        followedByUsernames.remove(user.getUsername());
    }

    public void addPost(Post c){
        postQueue.add(c);
    }

    public void addToPM(PM p){
        PmQueue.add(p);
    }

    public ConcurrentLinkedQueue<Notification> getNotifications() {
        return notifications;
    }

    public ConcurrentLinkedQueue<String> getFollowedByUsernames() {
        return followedByUsernames;
    }

    public ConcurrentLinkedQueue<String> getFollowingUsernames() {
        return followingUsernames;
    }

    public ConcurrentLinkedQueue<Post> getPostQueue() {
        return postQueue;
    }

    public ConcurrentLinkedQueue<String> getBlockedUsernames() {
        return BlockedUsernames;
    }

    public int getAge(){
        String format="";
        format = format + birthday.substring(6) + "-" + birthday.substring(3,5) + "-" + birthday.substring(0,2);
        LocalDate localDate = LocalDate.parse(format);
        Period p =Period.between(localDate, LocalDate.now());
        return p.getYears();
    }

    public synchronized boolean login(int connectionId){
        if(isLogged )
            return false;
        this.connectionID = connectionId;
        isLogged = true;
        return true;
    }

    public boolean logout(){
        if(!isLogged)
            return false;
        isLogged = false;
        return true;
    }
}
