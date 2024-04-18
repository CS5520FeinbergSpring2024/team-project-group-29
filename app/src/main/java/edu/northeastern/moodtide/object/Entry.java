package edu.northeastern.moodtide.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//Entry class represents the a single emotion entry in the app
public class Entry implements Parcelable {

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



    protected Entry(Parcel in) {
        category = in.readString();
        emotion = in.readString();
        triggers = new ArrayList<>();
        in.readList(triggers, Trigger.class.getClassLoader());
        note = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(emotion);
        dest.writeList(triggers);
        dest.writeString(note);
    }

    @Override
    public int describeContents() {
        return 0;
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


