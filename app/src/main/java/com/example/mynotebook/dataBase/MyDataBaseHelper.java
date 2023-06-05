package com.example.mynotebook.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MyDataBaseHelper extends SQLiteOpenHelper {

    public MyDataBaseHelper(Context context) {
        super(context, MyConstants.DATA_BASE_NAME, null, MyConstants.DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstants.TABLE_STRUCTURE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstants.DROP_TABLE);
        onCreate(db);

    }
}
