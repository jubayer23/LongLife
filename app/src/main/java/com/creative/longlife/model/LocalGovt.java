
package com.creative.longlife.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalGovt implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String govtName;
    public final static Creator<LocalGovt> CREATOR = new Creator<LocalGovt>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LocalGovt createFromParcel(Parcel in) {
            return new LocalGovt(in);
        }

        public LocalGovt[] newArray(int size) {
            return (new LocalGovt[size]);
        }

    }
    ;

    protected LocalGovt(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.govtName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LocalGovt() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGovtName() {
        return govtName;
    }

    public void setGovtName(String govtName) {
        this.govtName = govtName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(govtName);
    }

    public int describeContents() {
        return  0;
    }

}
