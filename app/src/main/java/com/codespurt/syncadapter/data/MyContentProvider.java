package com.codespurt.syncadapter.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codespurt.syncadapter.R;

/**
 * Created by CodeSpurt on 05-08-2017.
 */

public class MyContentProvider extends ContentProvider {

    private Context context;

    private static final String TAG = MyContentProvider.class.getSimpleName();
    public static String AUTHORITY = "";
    public static String BASE_PATH = "CodeSpurt";

    private static final int friends = 100;
    private static final int friend = 200;
    private static final int updateDb = 300;

    public static Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static Uri CONTENT_UPDATE_ALL_URI = Uri.parse(CONTENT_URI + "/updataDb");// haven't check
    public static String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/friends";
    public static String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/friend";
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private SQLiteDatabase database;
    private MySQLiteHelper helper;

    static {
        matcher.addURI(AUTHORITY, "/" + BASE_PATH, friends);
        matcher.addURI(AUTHORITY, "/" + BASE_PATH + "/#", friend);
        matcher.addURI(AUTHORITY, "/" + BASE_PATH + "/updateDb", updateDb);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        helper = new MySQLiteHelper(context);
        AUTHORITY = context.getResources().getString(R.string.content_authority);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        database = helper.getWritableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(MyTable.TABLE_NAME);

        Log.d(TAG, ">query, uri: " + uri + " selction: " + selection + " selctionArgs: " + selectionArgs);

        int type = matcher.match(uri);
        switch (type) {
            case friends:
                break;
            case friend:
                builder.appendWhere(MyTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
        }
        Cursor cursor = builder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        database = helper.getWritableDatabase();
        int type = matcher.match(uri);
        switch (type) {
            case (friends):
                Log.d(TAG, "Data Insert");
                database.insert(MyTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("fuck wrong content uri " + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH + "/0");//should be tested
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "Update. Try to match uri: " + uri);
        int type = matcher.match(uri);
        int toBeReturn = -2;
        switch (type) {
            case (updateDb):
                helper.onUpgrade(helper.getWritableDatabase(), 0, 1);
                toBeReturn = -1;
                break;
            default:
                break;
        }
        return toBeReturn;
    }
}