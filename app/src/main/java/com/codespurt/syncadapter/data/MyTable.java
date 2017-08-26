package com.codespurt.syncadapter.data;

/**
 * Created by CodeSpurt on 05-08-2017.
 */

public class MyTable {

    public static final String TABLE_NAME = "MyTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_names";
    public static final String COLUMN_SEX = "_sex";

    public static final String[] ALL_PROJECTION = {COLUMN_ID, COLUMN_NAME, COLUMN_SEX};
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_SEX + " text not null"
            + ");";
}