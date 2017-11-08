package com.creative.longlife.appdata;


import com.creative.longlife.model.Category;

public class GlobalAppAccess {

    private static final String BASE_URL = "http://longlife.com.ng/longlife/api/";

    public static final String URL_LOGIN = BASE_URL + "login.php";
    public static final String URL_REGISTER = BASE_URL + "register.php";
    public static final String URL_ALL_CATEGORYLIST = BASE_URL + "allCategories.php";
    public static final String URL_SELECT_CATEGORY = BASE_URL + "userSelectedCategories.php";
    public static final String URL_USER_CATEGORYLIST = BASE_URL + "user_categorylist.php";
    public static final String URL_SERVICE = BASE_URL + "servicelist.php";
    public static final String URL_STATES = BASE_URL + "nigerian_states.php";
    public static final String URL_LOCAL_GOVT = BASE_URL + "local_govt.php";



    public static final String  CAT_FAVOURITE = "Favourite";
    public static final String  CAT_FAVOURITE_ID = "-100";
    public static final String  CAT_EMERGENCY = "Emergency";
    public static final String  CAT_EMERGENCY_ID = "Emergency";

    public static Category default_category_fav = new Category(CAT_FAVOURITE_ID,CAT_FAVOURITE,"none");
    public static Category default_category_EMR = new Category(CAT_EMERGENCY_ID,CAT_EMERGENCY,"none");
}
