package edu.northeastern.moodtide.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Trigger implements Parcelable {
    String name;

    public Trigger(String name) {
        this.name = name;
    }

    protected Trigger(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Trigger> CREATOR = new Creator<Trigger>() {
        @Override
        public Trigger createFromParcel(Parcel in) {
            return new Trigger(in);
        }

        @Override
        public Trigger[] newArray(int size) {
            return new Trigger[size];
        }
    };

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
    }
}
