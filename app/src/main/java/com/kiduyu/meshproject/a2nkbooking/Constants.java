package com.kiduyu.meshproject.a2nkbooking;

/**
 * Class which contains values for different SharedPreferences value and URL
 */
class Constants {
    public static final String SHARED_PREFS_USER_DATA = "user_data";
    public static final String USER_DATA_OBJECT = "user_data_object";
    public static final String SUCCESSFUL_LOGIN_HISTORY = "successful_login_history";
    public static final String SUCCESSFUL_REGISTRATION_HISTORY = "successful_registration_history";

    //URL Addresses for Server
    public static final String LOGIN_URL= "http://the-bookhub.co.ke/2nk/login.php";
    public static final String SIGNUP;
    public static final String EDIT_DETAILS;
    public static final String FETCH_DETAILS;
    public static final String BUS_LIST;
    public static final String BUS_DETAILS;
    public static final String STOP_LIST;
    public static final String STOP_DETAILS;
    //for testing
    //set localhost = false for testing from webhost
    private static final Boolean localhost = false;
    private static final String ADDRESS;

    static {
        if (localhost) {
            ADDRESS = "http://the-bookhub.co.ke/2nk/";
        } else {
            ADDRESS = "http://the-bookhub.co.ke/2nk/";
        }
       // LOGIN = ADDRESS + "login.php";
        SIGNUP = ADDRESS + "register.php";
        EDIT_DETAILS = ADDRESS + "editdetails.php";
        FETCH_DETAILS = ADDRESS + "fetchdetails.php";
        BUS_LIST = ADDRESS + "buslist.php";
        STOP_LIST = ADDRESS + "stoplist.php";
        BUS_DETAILS = ADDRESS + "busdetails.php";
        STOP_DETAILS = ADDRESS + "stopdetails.php";
    }
}
