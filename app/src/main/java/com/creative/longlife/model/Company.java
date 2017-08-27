package com.creative.longlife.model;

/**
 * Created by comsol on 25-Aug-17.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact_person")
    @Expose
    private String contactPerson;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Parcelable.Creator<Company> CREATOR = new Creator<Company>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Company createFromParcel(Parcel in) {
            Company instance = new Company();
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((String) in.readValue((String.class.getClassLoader())));
            instance.email = ((String) in.readValue((String.class.getClassLoader())));
            instance.contactPerson = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));
            instance.status = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Company[] newArray(int size) {
            return (new Company[size]);
        }

    }
            ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(phone);
        dest.writeValue(email);
        dest.writeValue(contactPerson);
        dest.writeValue(address);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}