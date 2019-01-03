package com.u3xj.collegebar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;

    private static Context mCtx;

    private static final String KEY_ACCESS_TOKEN = "token";

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NICKNAME = "usernickname";
    private static final String KEY_COLLEGENAME = "usercollegename";
    private static final String KEY_GENDER = "usergender";
    private static final String KEY_DOB = "userdob";
    private static final String KEY_PROFILE_PICTURE = "userprofile";
    private static final String KEY_SOUND = "usersound";


    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean storeToken(String token)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }

    public static String getToken() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,"default");
    }





    public boolean userLogin(int user_id, String username, String nick_name,String collegename, String gender, String dob, String profileName)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID,user_id);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_NICKNAME,nick_name);
        editor.putString(KEY_COLLEGENAME,collegename);
        editor.putString(KEY_GENDER,gender);
        editor.putString(KEY_DOB,dob);
        editor.putString(KEY_PROFILE_PICTURE,profileName);
        editor.putString(KEY_SOUND,"0");
        editor.apply();
        return true;
    }

    public boolean isLoggedIn()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME,null) != null)
            return true;
        return false;
    }

    public boolean logout()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }


    public int getUserID()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_USER_ID, 0);

    }

    public String getUserName()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);

    }


    public String getNickName()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NICKNAME, null);
    }

    public String getCollegeName()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_COLLEGENAME, null);
    }

    public String getGender()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_GENDER, null);
    }

    public String getDOB()
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DOB, null);
    }

    public boolean storeProfile(String token)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PROFILE_PICTURE,token);
        editor.apply();
        return true;
    }

    public String getProfile() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PROFILE_PICTURE,"default");
    }

    public boolean storeSound(String token)
    {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SOUND,token);
        editor.apply();
        return true;
    }

    public String getStoreSound() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SOUND,"default");
    }
}


