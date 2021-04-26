package com.lionbiterclacclac.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedValues {

    public static int getInt(Context context, String key, int defVal){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, defVal);
    }

    public static void setInt(Context context, String key, int val){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static String getString(Context context, String key, String defVal){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, defVal);
    }

    public static void setString(Context context, String key, String val){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static void setBoolean(Context context, String key, boolean val){
        SharedPreferences settings = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defVal){
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defVal);
    }
}
