package com.creative.longlife.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * Created by comsol on 24-Jan-18.
 */

public class Reminder {
    String title;
    String body;
    String time;

    public Reminder(String title, String body, String time) {
        this.title = title;
        this.body = body;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public static class timeComparatorAesc implements Comparator<Reminder> {
        public int compare(Reminder chair1, Reminder chair2) {
            SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            try {
                java.util.Date date1 = readFormat.parse(chair1.getTime());
                java.util.Date date2 = readFormat.parse(chair2.getTime());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
    public  static   class timeComparatorDesc implements Comparator<Reminder> {
        public int compare(Reminder chair1, Reminder chair2) {
            SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
            try {
                java.util.Date date1 = readFormat.parse(chair1.getTime());
                java.util.Date date2 = readFormat.parse(chair2.getTime());
                return date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
