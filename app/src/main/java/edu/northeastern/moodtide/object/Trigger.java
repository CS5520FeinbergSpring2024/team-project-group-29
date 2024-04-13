package edu.northeastern.moodtide.object;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Trigger implements Parcelable  {
    String name;

    public Trigger(){this.name="";}

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check if the objects are the same instance
        if (o == null || getClass() != o.getClass()) return false; // Check for null and compare classes
        Trigger trigger = (Trigger) o; // Type cast
        return Objects.equals(name, trigger.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name); // Compute hash based on id and description
    }

}
