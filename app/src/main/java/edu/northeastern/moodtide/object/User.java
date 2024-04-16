package edu.northeastern.moodtide.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class User {

    private String id;
    private HashMap<String, HashMap<String, Entry>> data;
    private int streak;
    private String lastDate;
    private String notificationTime;
    private List<Trigger> myTriggers;

    public User() {
        id = "null";
        data = new HashMap<>();
        streak = 0;
        lastDate = "null";
        notificationTime = "null";
        myTriggers = new ArrayList<>();
    }

    public User(String id) {
        this.id = id;
        this.data = new HashMap<>();
        this.streak = 0;
        this.lastDate = "null";
        this.notificationTime = "null";
        this.myTriggers = new ArrayList<>();
    }
    public User(String id, List<Trigger> myTriggers) {
        this.id = id;
        this.data = new HashMap<>();
        this.streak = 0;
        this.lastDate = "null";
        this.notificationTime = "null";
        this.myTriggers = myTriggers;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HashMap<String, HashMap<String, Entry>> getData() {
        return data;
    }

    public void setData(HashMap<String, HashMap<String, Entry>> data) {
        this.data = data;
    }

    public int getStreak() { return streak; }

    public void setStreak(int streak){ this.streak=streak; }

    public void setNotificationTime(String notificationTime){this.notificationTime=notificationTime;}

    public String getLastDate(){ return lastDate; }

    public void setLastDate(String lastDate){ this.lastDate=lastDate;}

    public String getNotificationTime(){return this.notificationTime;}

    public List<Trigger> getMyTriggers() {
        return myTriggers;
    }

    public void setMyTriggers(List<Trigger> myTriggers) {
        this.myTriggers = myTriggers;
    }
}
