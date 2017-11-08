package com.creative.longlife.model;

/**
 * Created by comsol on 23-Sep-17.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalGovts implements Parcelable
{

    @SerializedName("govts")
    @Expose
    private List<Govt> govts = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;

    protected LocalGovts(Parcel in) {
        govts = in.createTypedArrayList(Govt.CREATOR);
        message = in.readString();
    }

    public static final Creator<LocalGovts> CREATOR = new Creator<LocalGovts>() {
        @Override
        public LocalGovts createFromParcel(Parcel in) {
            return new LocalGovts(in);
        }

        @Override
        public LocalGovts[] newArray(int size) {
            return new LocalGovts[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<Govt> getGovts() {
        return govts;
    }

    public void setGovts(List<Govt> govts) {
        this.govts = govts;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(govts);
        parcel.writeString(message);
    }
}