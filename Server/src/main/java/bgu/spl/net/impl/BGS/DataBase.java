package bgu.spl.net.impl.BGS;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class DataBase {
    //fields
    ConcurrentHashMap<String,User> users;
    ConcurrentLinkedQueue<String> postsAndPMs;
    AtomicInteger numOfLoggedInUsers;

    private static class DataBaseHolder{
        private static DataBase instance = new DataBase();

    }
    //private constructor - singleton impl
    private DataBase(){
        users = new ConcurrentHashMap<>();
        postsAndPMs = new ConcurrentLinkedQueue<>();
        numOfLoggedInUsers = new AtomicInteger(0);
    }

    public static DataBase getInstance(){
        return DataBase.DataBaseHolder.instance;
    }

    public ConcurrentHashMap<String,User> getUsers() {
        return users;
    }

    public User getUser(String name){
        return users.get(name);
    }

    public void addPostOrPM(String content) {
       postsAndPMs.add(content);
    }

    public void incLoggedIn(){
        numOfLoggedInUsers.incrementAndGet();
    }

    public void decLoggedIn(){
        numOfLoggedInUsers.decrementAndGet();
    }

    public int getNumOfLoggedIn(){
        return numOfLoggedInUsers.get();
    }

}
