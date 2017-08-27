package com.creative.longlife.appdata;


import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class GlobalAppAccess {

    private static final String BASE_URL = "http://longlife.com.ng/longlife/api/";

    public static final String URL_LOGIN = BASE_URL + "login.php";
    public static final String URL_REGISTER = BASE_URL + "register.php";
    public static final String URL_ALL_CATEGORYLIST = BASE_URL + "allCategories.php";
    public static final String URL_SELECT_CATEGORY = BASE_URL + "userSelectedCategories.php";
    public static final String URL_USER_CATEGORYLIST = BASE_URL + "user_categorylist.php";
    public static final String URL_SERVICE = BASE_URL + "servicelist.php";



    public static final String  CAT_FAVOURITE = "Favourite";
    public static final String  CAT_EMERGENCY = "Emergency";
    public static final String  CAT_OTHERS = "Others";
    public static final String default_categories[] = {CAT_EMERGENCY,CAT_FAVOURITE,CAT_OTHERS};
}
