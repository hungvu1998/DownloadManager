package com.example.downloadmanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PreferencesUtility {
    private static final String PREFERENCE_NAME = "com.example.downloadmanager.PREF";
    private static final String SHOULD_ASK_STORAGE_PREF = "storagePermission";
    private static final String DOWNLOAD_LIST_PREF = "downloadList";

    public static void setShouldAskWriteStoragePermission(Context context, boolean value,String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHOULD_ASK_STORAGE_PREF,value);
        editor.apply();
    }

    public static boolean getShouldAskWriteStoragePermission(Context context,String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHOULD_ASK_STORAGE_PREF,false);
    }

    public static List<DownloadModel> getDownloadList(Context context,String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(DOWNLOAD_LIST_PREF,null);
        if(json != null){
            return new Gson().fromJson(json, new TypeToken<List<DownloadModel>>(){}.getType());
        } else {
            return null;
        }
    }

    public static void setDownloadList(Context context, List<DownloadModel> list,String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(list);
        editor.putString(DOWNLOAD_LIST_PREF,json);
        editor.apply();
    }
}
