package com.codespurt.syncadapter.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by CodeSpurt on 05-08-2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = MySQLiteHelper.class.getSimpleName();
    public static final String dbName = "FriendsDb";
    public static final int version = 1;

    public MySQLiteHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade the friend database. All data will be deleted oldVersion: " + oldVersion + " newVersion: " + newVersion);
        db.execSQL("DROP TABLE " + MyTable.TABLE_NAME);
        db.execSQL(MyTable.CREATE_TABLE);
    }
}
