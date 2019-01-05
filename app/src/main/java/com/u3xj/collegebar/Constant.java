package com.u3xj.collegebar;

public class Constant
{

    private static final String ROOT_URL = "http://192.168.43.159/android/v1/";
    /*
        private static final String ROOT_URL = "http://collegebar.000webhostapp.com/v1/";*/
    public static final String URL_REGISTER = ROOT_URL+"registerUser.php";
    public static final String URL_LOGIN = ROOT_URL+"userLogin.php";
    public static final String URL_STUDENT_LIST = ROOT_URL+"studentList.php";
    public static final String URL_STUDENT_MESSAGE_SEND = ROOT_URL+"studentAddMessage.php";
    public static final String URL_STUDENT_MESSAGE_GET = ROOT_URL+"studentGetMessage.php";
    public static final String URL_PUT_TOKEN = ROOT_URL+"storeGCMToken.php";
    public static final String URL_SEND_NOTIFICATION = ROOT_URL+"sendNotification.php";
    public static final String URL_DELETE_NOTIFICATION = ROOT_URL+"deleteGCMToken.php";
    public static final String PUSH_NOTIFICATION = "notification";
    public static final String URL_MESSAGE_LIST = ROOT_URL+"getStudentMessageList.php";
    public static final String URL_FLAG_UPDATE = ROOT_URL+"updateFlag.php";
    public static final String URL_APP_UPDATE = ROOT_URL+"getURLApp.php";
    public static final String URL_USERNAME_EXIST = ROOT_URL+"userNameExist.php";
    public static final String URL_CHANGE_PASSWORD = ROOT_URL+"updatePassword.php";
    public static final String URL_UPLOAD_IMAGE = ROOT_URL+"uploadProfile.php";
    public static final String URL_OFFLINE_STATUS = ROOT_URL+"offlineStatus.php";

}
