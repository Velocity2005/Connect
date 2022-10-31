package com.example.seniormatch.utilities;

import android.content.SharedPreferences;
import android.content.Context;

public class PreferenceManager {

    private final SharedPreferences sP;

    public PreferenceManager(Context c){
        sP = c.getSharedPreferences(Constants.KEY_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }

    public void putBoolean(String key, Boolean value){
        SharedPreferences.Editor editor = sP.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return sP.getBoolean(key,false);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = sP.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return sP.getString(key,null);
    }

    public void clear(){
        SharedPreferences.Editor editor = sP.edit();
        editor.clear();
        editor.apply();
    }
}
