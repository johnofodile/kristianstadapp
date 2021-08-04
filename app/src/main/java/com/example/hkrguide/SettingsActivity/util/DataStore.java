package com.example.hkrguide.SettingsActivity.util;

import android.content.Context;

import androidx.preference.PreferenceDataStore;

import java.util.List;

public class DataStore extends PreferenceDataStore {

    private DatabaseHelper dbHelper;

    public DataStore(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    @Override
    public void putString(String key, String value) {
        if(!dbHelper.update(key, value)){
            dbHelper.insert(key, value);
        }
    }

    @Override
    public String getString(String key, String defValue) {
        List<String> result = dbHelper.get(key);
        if(result.size() == 0){
            dbHelper.insert(key, defValue);
            return defValue;
        } else if(result.size() == 1){
            return result.get(0);
        } else {
            dbHelper.delete(key);
            dbHelper.insert(key, defValue);
            return defValue;
        }
    }

    @Override
    public void putBoolean(String key, boolean value) {
        putString(key, Boolean.toString(value));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.parseBoolean(getString(key, Boolean.toString(defValue)));
    }
}

