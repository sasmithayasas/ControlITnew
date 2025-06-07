package com.example.controlit;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEBERME = "rememberMe";
    private static final String IS_LOGIN = "IsLoggedIn";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONUSERNAME = "sessionUsername";
    public static final String KEY_SESSIONPASSWORD = "sessionPassword";


    public SessionManager(Context _context, String sessionName) {
        context = _context;
        userSession = _context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String username, String password, String email) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_EMAIL, email);

        editor.commit();


    }

    public HashMap<String, String> getUserDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_USERNAME, userSession.getString(KEY_USERNAME, null));
        userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));

        return userData;

    }

    public boolean checkLogin() {
        if (!this.userSession.getBoolean(IS_LOGIN, false)) {
            return false;
        } else {
            return true;
        }
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

    public void createRememberMeSession(String username, String password) {

        editor.putBoolean(IS_REMEMBERME, true);

        editor.putString(KEY_SESSIONUSERNAME, username);
        editor.putString(KEY_SESSIONPASSWORD, password);


        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailsFromSession() {
        HashMap<String, String> rememberMeData = new HashMap<String, String>();
        rememberMeData.put(KEY_SESSIONUSERNAME, userSession.getString(KEY_SESSIONUSERNAME, null));
        rememberMeData.put(KEY_SESSIONPASSWORD, userSession.getString(KEY_SESSIONPASSWORD, null));
        return rememberMeData;


    }

    public boolean checkRememberMe() {
        if (!this.userSession.getBoolean(IS_REMEMBERME, false)) {
            return false;
        } else {
            return true;
        }
    }
}
