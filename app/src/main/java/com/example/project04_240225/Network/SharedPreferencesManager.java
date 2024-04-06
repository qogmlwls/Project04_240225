package com.example.project04_240225.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_SESSION_COOKIE = "sessionCookie";
    private static final String KEY_LoginType = "loginType";
    private static final String KEY_LoginType2 = "loginType2";

    private static final String DEFAULT = "default";
    private static final String KAKAO = "KAKAO";
    private static final String NAVER = "NAVER";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveSessionCookie(String cookieValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SESSION_COOKIE, cookieValue);
        editor.apply();
    }

    public String getSessionCookie() {
        return sharedPreferences.getString(KEY_SESSION_COOKIE, null);
    }

    public void saveLoginType(String cookieValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LoginType, cookieValue);
        boolean result = editor.commit();
        Log.i("saveLoginType",Boolean.toString(result));
    }


    public void removeLoginType(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_LoginType);
        boolean result = editor.commit();
    }
    public String getLoginType() {
        return sharedPreferences.getString(KEY_LoginType, null);
    }

    public void saveLoginType2(String cookieValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LoginType2, cookieValue);
        boolean result = editor.commit();
        Log.i("saveLoginType2",Boolean.toString(result));
    }
    public void removeLoginType2(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_LoginType2);
        boolean result = editor.commit();
    }
    public String getLoginType2() {
        return sharedPreferences.getString(KEY_LoginType2, null);
    }




}