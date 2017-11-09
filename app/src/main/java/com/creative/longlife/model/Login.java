package com.creative.longlife.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jubayer on 8/22/2017.
 */

public class Login implements Serializable
{

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("login_type")
    @Expose
    private User login_type;
    private final static long serialVersionUID = 4275859057067326939L;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User getLogin_type() {
        return login_type;
    }

    public void setLogin_type(User login_type) {
        this.login_type = login_type;
    }
}