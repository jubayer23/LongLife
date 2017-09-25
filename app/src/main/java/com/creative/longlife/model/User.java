package com.creative.longlife.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jubayer on 8/21/2017.
 */

public class User implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gdCode")
    @Expose
    private String gdCode;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("localGovtName")
    @Expose
    private String localGovtName;
    @SerializedName("sex")
    @Expose
    private String sex;
    @SerializedName("dob")
    @Expose
    private String dob;
    private final static long serialVersionUID = -664237076134602702L;

    public User(String id, String name, String email, String gdCode, String stateName, String localGovtName, String sex, String dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gdCode = gdCode;
        this.stateName = stateName;
        this.localGovtName = localGovtName;
        this.sex = sex;
        this.dob = dob;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGdCode() {
        return gdCode;
    }

    public void setGdCode(String gdCode) {
        this.gdCode = gdCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLocalGovtName() {
        return localGovtName;
    }

    public void setLocalGovtName(String localGovtName) {
        this.localGovtName = localGovtName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
