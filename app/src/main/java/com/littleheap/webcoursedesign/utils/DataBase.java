package com.littleheap.webcoursedesign.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wangs on 2017/10/18.
 */

public class DataBase {


    public static void insertDataBase(MyDatabaseHelper dbHelper, String database, String person, String number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("person", person);
        values.put("number", number);
        db.insert(database, null, values);
        values.clear();
    }

    public static void updateDataBase_number(MyDatabaseHelper dbHelper, String database, String person, String number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        db.update(database, values, "person = ?", new String[]{person});
        values.clear();
    }

    public static String selectDataBase(MyDatabaseHelper dbHelper, String database) {
        String whole = "";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(database, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String _person = cursor.getString(cursor.getColumnIndex("person"));
                String _number = cursor.getString(cursor.getColumnIndex("number"));
                whole = whole + _person + ":" + _number + ";";
            } while (cursor.moveToNext());
        }
        cursor.close();
        return whole;
    }

    public static void deleteDataBase(MyDatabaseHelper dbHelper, String database, String person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(database, "person = ?", new String[]{person});
    }
}
