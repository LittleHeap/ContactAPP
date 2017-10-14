package com.littleheap.webcoursedesign.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by wangs on 2017/10/14.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CONTACT = "create table ContactA (person text primary key, number text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACT);
        Toast.makeText(mContext, "Create Contact Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists ContactA");
        onCreate(db);
    }
}
