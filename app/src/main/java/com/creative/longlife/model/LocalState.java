
package com.creative.longlife.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalState implements Parcelable
{

    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("local_govts")
    @Expose
    private List<LocalGovt> localGovts = null;


    public LocalState() {
        this.localGovts = new ArrayList<>();
    }
    public LocalState(List<LocalGovt> localGovts) {
        this.localGovts = localGovts;
    }

    protected LocalState(Parcel in) {
        stateName = in.readString();
        localGovts = in.createTypedArrayList(LocalGovt.CREATOR);
    }

    public static final Creator<LocalState> CREATOR = new Creator<LocalState>() {
        @Override
        public LocalState createFromParcel(Parcel in) {
            return new LocalState(in);
        }

        @Override
        public LocalState[] newArray(int size) {
            return new LocalState[size];
        }
    };

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public List<LocalGovt> getLocalGovts() {
        return localGovts;
    }

    public void setLocalGovts(List<LocalGovt> localGovts) {
        this.localGovts = localGovts;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(stateName);
        dest.writeList(localGovts);
    }

    public int describeContents() {
        return  0;
    }

}
