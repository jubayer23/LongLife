package com.creative.longlife.model;

/**
 * Created by jubayer on 8/21/2017.
 */

public class User {
    int id;
    String name;
    String email;
    String sex;
    String location;

    public User(int id, String name, String email, String sex, String location) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
