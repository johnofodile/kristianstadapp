package com.example.hkrguide.SettingsActivity.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "settings.db", null, 1); // Create a database with version 1
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE settings(name TEXT PRIMARY KEY, value TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS settings");
    }

    public Boolean insert(String name, String value) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("value", value);

        return DB.insert("settings", null, contentValues) != -1;
    }


    public Boolean update(String name, String value) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("value", value);

        return DB.update("settings", contentValues, "name=?", new String[]{name}) > 0;
    }


    public Boolean delete(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();

        return DB.delete("settings", "name=?", new String[]{name}) > 0;
    }

    public List<String> get(String name) {
        SQLiteDatabase DB = this.getReadableDatabase();

        try (Cursor cursor = DB.query("settings", new String[]{"value"}, "name = ?", new String[]{name}, null, null, null)) {

            ArrayList<String> list = new ArrayList<>();

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String value = cursor.getString(cursor.getColumnIndex("value"));
                    list.add(value);
                    cursor.moveToNext();
                }
            }

            return list;
        }
    }
}