
package com.creative.longlife.model;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class States implements Parcelable
{

    @SerializedName("states")
    @Expose
    private List<State> states = null;
    @SerializedName("success")
    @Expose
    private Integer success;


    protected States(Parcel in) {
        states = in.createTypedArrayList(State.CREATOR);
    }

    public static final Creator<States> CREATOR = new Creator<States>() {
        @Override
        public States createFromParcel(Parcel in) {
            return new States(in);
        }

        @Override
        public States[] newArray(int size) {
            return new States[size];
        }
    };

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(states);
        dest.writeValue(success);
    }

    public int describeContents() {
        return  0;
    }

}
