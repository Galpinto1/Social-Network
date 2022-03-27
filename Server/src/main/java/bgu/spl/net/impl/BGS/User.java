package bgu.spl.net.impl.BGS;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

    //fields
    private String username;
    private String password;
    private String birthday;
    private AtomicBoolean loggedIn;
    private AtomicInteger numOfPosts;
    private ConcurrentHashMap<String,User> followers;
    private ConcurrentHashMap<String,User> following;
    private ConcurrentHashMap<String, User> blocked;
    private int connectionId;

    //constructor
    public User(String username, String password, String birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        loggedIn = new AtomicBoolean(false);
        numOfPosts = new AtomicInteger(0);
        followers = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
        blocked = new ConcurrentHashMap<>();
        connectionId = -1;
    }
    public String getPassword(){
        return password;
    }
    public void setConnectionId(int id) { connectionId = id;}
    public boolean isLoggedIn(){
        return loggedIn.get();
    }
    public void Login(){
       loggedIn.set(true);
    }
    public void Logout(){
        loggedIn.set(false);
    }

    public boolean following(String name) {
        User user = following.get(name);
        if(user == null) {
            return false;
        }
        else {
            return true;
        }
    }
    public void follow(String name, User other) {
        following.put(name, other);
    }

    public void unfollow(String username) {
        following.remove(username);
    }

    public void addFollower(String name, User other) {
        followers.put(name, other);
    }

    public void removeFollower(String username) {
        followers.remove(username);
    }

    public String getUsername(){
        return username;
    }

    public void block(String name, User user) {
        blocked.putIfAbsent(name, user);
        following.remove(name);
        followers.remove(name);
    }

    public boolean blocked(String name) {
        User user = blocked.get(name);
        if(user == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getConnectionId(){
        return connectionId;
    }

    public ConcurrentHashMap<String,User> getFollowers(){
        return followers;
    }

    public void incPosts() {
        numOfPosts.incrementAndGet();
    }

    public int getNumOfFollowing() {
        return following.size();
    }

    public int getNumOfPosts() {
        return numOfPosts.get();
    }

    public int getAge(){
        String yearOfBirth = birthday.substring(6);
        int year = Integer.parseInt(yearOfBirth);
        return 2022-year;
    }

}
