package com.creative.longlife.sharedprefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.creative.longlife.BuildConfig;
import com.creative.longlife.model.Notification;
import com.creative.longlife.model.Reminder;
import com.creative.longlife.model.Service;
import com.creative.longlife.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jubayer on 6/6/2017.
 */


public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static Gson GSON = new Gson();
    // Sharedpref file name
    private static final String PREF_NAME = BuildConfig.APPLICATION_ID;

    private static final String KEY_USER = "user";

    private static final String KEY_RECEIVED_CARD_OBJ = "received_card_obj";
    private static final String KEY_FAV_SERVICE = "fav_service";
    private static final String KEY_EMERGENCY_SERVICE = "emr_service";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_REMINDER = "reminder";
    private static final String KEY_EMAIL_CACHE = "key_email_cache";
    private static final String KEY_NOTIFICATION_COUNTER = "key_notification_counter";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public void setEmailCache(String obj) {
        editor = pref.edit();

        editor.putString(KEY_EMAIL_CACHE, obj);

        // commit changes
        editor.commit();
    }
    public String getEmailCache() {
        return pref.getString(KEY_EMAIL_CACHE,"");
    }
    public void setNewNotificationCounter(int obj) {
        editor = pref.edit();

        editor.putInt(KEY_NOTIFICATION_COUNTER, obj);

        // commit changes
        editor.commit();
    }
    public int getNewNotificationCounter() {
        return pref.getInt(KEY_NOTIFICATION_COUNTER,0);
    }
    public void setUserProfile(User obj) {
        editor = pref.edit();

        editor.putString(KEY_USER, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }

    public void setUserProfile(String obj) {
        editor = pref.edit();

        editor.putString(KEY_USER, obj);

        // commit changes
        editor.commit();
    }

    public User getUserProfile() {

        String gson = pref.getString(KEY_USER, "");
        if (gson.isEmpty()) return null;
        return GSON.fromJson(gson, User.class);
    }


    public void setFavServices(List<Service> obj) {
        editor = pref.edit();

        editor.putString(KEY_FAV_SERVICE, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }

    public void setFavServices(String obj) {
        editor = pref.edit();

        editor.putString(KEY_FAV_SERVICE, obj);

        // commit changes
        editor.commit();
    }


    public List<Service> getFavServices() {

        List<Service> productFromShared = new ArrayList<>();

        String gson = pref.getString(KEY_FAV_SERVICE, "");

        if (gson.isEmpty()) return productFromShared;

        Type type = new TypeToken<List<Service>>() {
        }.getType();
        productFromShared = GSON.fromJson(gson, type);

        return productFromShared;
    }


    public void setEmergencyServices(List<Service> obj) {
        editor = pref.edit();

        editor.putString(KEY_EMERGENCY_SERVICE, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }

    public void setEmergencyServices(String obj) {
        editor = pref.edit();

        editor.putString(KEY_EMERGENCY_SERVICE, obj);

        // commit changes
        editor.commit();
    }


    public List<Service> getEmergencyServices() {

        List<Service> productFromShared = new ArrayList<>();

        String gson = pref.getString(KEY_EMERGENCY_SERVICE, "");

        if (gson.isEmpty()) return productFromShared;

        Type type = new TypeToken<List<Service>>() {
        }.getType();
        productFromShared = GSON.fromJson(gson, type);

        return productFromShared;
    }


    public void setNotifications(List<Notification> obj) {
        editor = pref.edit();

        editor.putString(KEY_NOTIFICATION, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }

    public void setNotifications(String obj) {
        editor = pref.edit();

        editor.putString(KEY_NOTIFICATION, obj);

        // commit changes
        editor.commit();
    }


    public List<Notification> getNotifications() {

        List<Notification> productFromShared = new ArrayList<>();

        String gson = pref.getString(KEY_NOTIFICATION, "");

        if (gson.isEmpty()) return productFromShared;

        Type type = new TypeToken<List<Notification>>() {
        }.getType();
        productFromShared = GSON.fromJson(gson, type);

        return productFromShared;
    }


    public void setReminders(List<Reminder> obj) {
        editor = pref.edit();

        editor.putString(KEY_REMINDER, GSON.toJson(obj));

        // commit changes
        editor.commit();
    }

    public void setReminders(String obj) {
        editor = pref.edit();

        editor.putString(KEY_REMINDER, obj);

        // commit changes
        editor.commit();
    }


    public List<Reminder> getReminders() {

        List<Reminder> productFromShared = new ArrayList<>();

        String gson = pref.getString(KEY_REMINDER, "");

        if (gson.isEmpty()) return productFromShared;

        Type type = new TypeToken<List<Reminder>>() {
        }.getType();
        productFromShared = GSON.fromJson(gson, type);

        return productFromShared;
    }

}