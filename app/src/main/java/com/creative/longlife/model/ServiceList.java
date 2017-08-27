package com.creative.longlife.model;

/**
 * Created by comsol on 25-Aug-17.
 */

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceList implements Parcelable {

    @SerializedName("services")
    @Expose
    private List<Service> services = null;
    @SerializedName("success")
    @Expose
    private Integer success;
    public final static Parcelable.Creator<ServiceList> CREATOR = new Creator<ServiceList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ServiceList createFromParcel(Parcel in) {
            ServiceList instance = new ServiceList();
            in.readList(instance.services, (com.creative.longlife.model.Service.class.getClassLoader()));
            instance.success = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public ServiceList[] newArray(int size) {
            return (new ServiceList[size]);
        }

    };

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(services);
        dest.writeValue(success);
    }

    public int describeContents() {
        return 0;
    }

}