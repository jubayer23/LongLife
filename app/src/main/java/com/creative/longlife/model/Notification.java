package com.creative.longlife.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jubayer on 1/23/2018.
 */

public class Notification implements Parcelable{
    String title;
    String body;
    String img_url;

    public Notification(String title, String body, String img_url) {
        this.title = title;
        this.body = body;
        this.img_url = img_url;
    }

    protected Notification(Parcel in) {
        title = in.readString();
        body = in.readString();
        img_url = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(img_url);
    }
}
