package edu.northeastern.moodtide.object;

import java.util.ArrayList;
import java.util.List;

public class Entry {

    private String category;
    private String emotion;
    private List<Trigger> triggers;
    private String note;


    public Entry(){
        this.category="null";
        this.emotion="null";
        this.triggers=new ArrayList<>();
        this.note="null";
    }
    public Entry(String category, String emotion, List<Trigger> triggers, String note){
        this.category=category;
        this.emotion=emotion;
        this.triggers=triggers;
        this.note=note;
    }
    // constructor when note is empty
    public Entry(String category, String emotion, List<Trigger> triggers){
        this.category=category;
        this.emotion=emotion;
        this.triggers=triggers;
        this.note= "";
    }



    // Getter for category
    public String getCategory() {
        return category;
    }

    // Setter for category
    public void setCategory(String category) {
        this.category = category;
    }

    // Getter for emotion
    public String getEmotion() {
        return emotion;
    }

    // Setter for emotion
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    // Getter for triggers
    public List<Trigger> getTriggers() {
        return triggers;
    }

    // Setter for triggers
    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    // Getter for note
    public String getNote() {
        return note;
    }

    // Setter for note
    public void setNote(String note) {
        this.note = note;
    }
}


