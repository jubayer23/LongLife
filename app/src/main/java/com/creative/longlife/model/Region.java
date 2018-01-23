
package com.creative.longlife.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Region implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("local_states")
    @Expose
    private List<LocalState> localStates = null;

    public Region() {
        this.localStates = new ArrayList<>();
    }

    public Region(List<LocalState> localStates) {

        this.localStates = localStates;
    }

    protected Region(Parcel in) {
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
        localStates = in.createTypedArrayList(LocalState.CREATOR);
    }

    public static final Creator<Region> CREATOR = new Creator<Region>() {
        @Override
        public Region createFromParcel(Parcel in) {
            return new Region(in);
        }

        @Override
        public Region[] newArray(int size) {
            return new Region[size];
        }
    };

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<LocalState> getLocalStates() {
        return localStates;
    }

    public void setLocalStates(List<LocalState> localStates) {
        this.localStates = localStates;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(localStates);
    }

    public int describeContents() {
        return  0;
    }

}
