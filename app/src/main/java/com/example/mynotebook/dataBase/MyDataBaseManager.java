package com.example.mynotebook.dataBase;

import static com.example.mynotebook.dataBase.MyConstants.DESCRIPTION;
import static com.example.mynotebook.dataBase.MyConstants.TABLE_NAME;
import static com.example.mynotebook.dataBase.MyConstants.TITLE;
import static com.example.mynotebook.dataBase.MyConstants.URI;
import static com.example.mynotebook.dataBase.MyConstants._ID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mynotebook.adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

public class MyDataBaseManager {
    private Context context;
    private MyDataBaseHelper myDataBaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MyDataBaseManager(Context context) {
        this.context = context;
        myDataBaseHelper = new MyDataBaseHelper(context);
    }

    public void openDataBase() {
        sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
    }

    public void insertToDataBAse(String title, String description, String uri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, title);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(URI, uri);
        sqLiteDatabase.insert(MyConstants.TABLE_NAME, null, contentValues);
    }

    public void deleteFromDataBase(int id) {
        String selection = _ID + "=" + id;
        sqLiteDatabase.delete(TABLE_NAME, selection, null);
    }

    public void updateFromDataBase(String title, String description, String uri, int id) {
        String selection = _ID + "=" + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, title);
        contentValues.put(DESCRIPTION, description);
        contentValues.put(URI, uri);
        sqLiteDatabase.update(TABLE_NAME, contentValues, selection, null);
    }

    public void getFromDataBase(String searchText, OnDataReceived onDataReceived) {
        List<ListItem> tempList = new ArrayList<>();
        String selection = TITLE + " like ?";
        Cursor cursor = sqLiteDatabase.query(MyConstants.TABLE_NAME, null, selection, new String[]{"%" + searchText + "%"},
                null, null, null);
        while (cursor.moveToNext()) {
            ListItem item = new ListItem();
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(TITLE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
            @SuppressLint("Range") String uri = cursor.getString(cursor.getColumnIndex(URI));
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(_ID));
            item.setTitle(title);
            item.setDescription(description);
            item.setUri(uri);
            item.setId(id);
            tempList.add(item);

        }
        cursor.close();
        onDataReceived.onReceived(tempList);
    }

    public void closeDataBase() {
        myDataBaseHelper.close();
    }
}
