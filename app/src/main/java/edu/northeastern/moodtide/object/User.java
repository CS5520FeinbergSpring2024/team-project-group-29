package edu.northeastern.moodtide.object;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private List<Entry> data;
    private int streak;
    private String lastDate;

    public User() {
        id = "null";
        data = new ArrayList<>();
        streak = 0;
        lastDate = "null";
    }

    public User(String id) {
        this.id = id;
        this.data = new ArrayList<>();
        this.streak = 0;
        this.lastDate = "null";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Entry> getData() {
        return data;
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }

    public int getStreak() { return streak; }

    public void setStreak(int streak){ this.streak=streak; }

    public String getLastDate(){ return lastDate; }

    public void setLastDate(String lastDate){ this.lastDate=lastDate;}
}
