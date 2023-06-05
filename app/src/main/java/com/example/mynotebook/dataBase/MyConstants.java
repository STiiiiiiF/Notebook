package com.example.mynotebook.dataBase;

public class MyConstants {
    public static final String EDIT_STATE = "edit_state";
    public static final String LIST_ITEM_INTENT = "list_item_intent";
    public static final String TABLE_NAME = "myTable";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String URI = "uri";
    public static final String DATA_BASE_NAME = "myDataBase.db";
    public static final int DATA_BASE_VERSION = 3;
    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + DESCRIPTION + " TEXT," + URI + " TEXT)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
