package com.creative.longlife.model;

/**
 * Created by jubayer on 8/23/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Category implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("priority")
    @Expose
    private String priority;
    private final static long serialVersionUID = 1111230700846377460L;

    public Category(String id, String name, String description, String priority) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        priority = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(priority);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class priorityOrderDescending implements Comparator<Category> {
        public int compare(Category chair1, Category chair2) {

            int date1 = Integer.parseInt(chair1.getPriority());
            int date2 = Integer.parseInt(chair2.getPriority());
            return date2 - date1;

        }
    }

    public static class idOrderAscending implements Comparator<Category> {
        public int compare(Category chair1, Category chair2) {

            int date1 = Integer.parseInt(chair1.getId());
            int date2 = Integer.parseInt(chair2.getId());
            return date1 - date2 ;

        }
    }
}