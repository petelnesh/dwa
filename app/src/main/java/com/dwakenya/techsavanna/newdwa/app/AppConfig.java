package com.dwakenya.techsavanna.newdwa.app;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class AppConfig {
    public static String SERVER_URL = "https://techsavanna.net:8181/dwa/";

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "DWA";
    // Camera activity request codes
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int GALLERY_IMAGE_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    //Start of shared preferences
    public static SharedPreferences preferences ;
    public static String prefString ="appBundle";

    public static String FIRST_NAME = "fname";
    public static String LAST_NAME = "lname";
    public static String IDNO = "idno";
    public static String PHONE = "phone";
    public static String EMAIL = "email";
    public static String COUNTY_TOWN = "county_town_id";
    public static String PASSWORD = "password";
    public static String COORDINATES = "coordinates";
    public static String LOCNAME = "locnmae";


    public static String ORGANIZATION = "org";
    public static String GENDER = "gender";
    public static String AGE = "age";
    public static String MARITAL_STATUS = "marital_status";
    public static String TRIBE = "tribe";



    public static String CITY = "city";
    public static String STATE = "state";
    public static String TIME_TO_CALL = "time_to_call";
    public static String AVAILABLE_FROM = "available_from";
    public static String CAN_RELOCATE = "can_relocate";
    public static String CURRENT_EMPLOYER = "current_emp";
    public static String CURRENT_PAY = "current_pay";
    public static String DESIRED_PAY = "desired_pay";

    public static String LENGTH = "length";
    public static String REASON = "reason";
    public static String REFEREE = "referee";
    public static String RELIGION = "religion";
    public static String CHILDREN = "children";

    public static String USER_ID = "user_id";

    public static ArrayList<String> SPECIALITIES = new ArrayList<>();

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public static  String GET_URL="https://techsavanna.net:8181/dwa/getPhone.php";
    public static  int GET_URL_REQUEST_CODE = 1;

}

