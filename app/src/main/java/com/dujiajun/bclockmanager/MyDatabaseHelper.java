package com.dujiajun.bclockmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cqduj on 2017/05/23.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final String CREATE_TABLE = "create table Clocks (id integer primary key autoincrement"
            + ",hour integer"
            + ",minute integer"
            + ",open integer"
            + ");";
    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
