package edu.northeastern.moodtide.object;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private List<Entry> data;

    public User() {
        id = "default";
        this.data = new ArrayList<>();
    }

    public User(String id) {
        this.id = id;
        this.data = new ArrayList<>();
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
}
