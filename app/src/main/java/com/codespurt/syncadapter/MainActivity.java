package com.codespurt.syncadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codespurt.syncadapter.data.MyContentProvider;
import com.codespurt.syncadapter.data.MyTable;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private Button addAccount, requestSync, queryAll, random, recreate;
    private TextView data;

    public static String[] nameList = {"sam", "kam", "jay", "Oscar", "juicy", "bitch", "gay"};
    public static String[] sexList = {"male", "female"};
    public static final String ACCOUNT = "default_account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilse);

        addAccount = (Button) findViewById(R.id.button_addaccount);
        requestSync = (Button) findViewById(R.id.button_requestsync);
        queryAll = (Button) findViewById(R.id.button_query_cp);
        random = (Button) findViewById(R.id.button_addrandomfive);
        recreate = (Button) findViewById(R.id.button_recreate);
        data = (TextView) findViewById(R.id.textview_display);
        data.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void onResume() {
        super.onResume();
        addAccount.setOnClickListener(this);
        requestSync.setOnClickListener(this);
        queryAll.setOnClickListener(this);
        random.setOnClickListener(this);
        recreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_addaccount:
                createAccount();
                break;
            case R.id.button_requestsync:
                requestSync();
                break;
            case R.id.button_query_cp:
                displayQuery(query());
                break;
            case R.id.button_addrandomfive:
                insertRandomFive();
                break;
            case R.id.button_recreate:
                showMessage(recreateDb() + "");
                break;
        }
    }

    private void createAccount() {
        AccountManager manager = AccountManager.get(this);
        manager.addAccount(getResources().getString(R.string.account_type), getResources().getString(R.string.account_auth_token_type), null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bundle = future.getResult();
                    String name = bundle.getString(AccountManager.KEY_ACCOUNT_NAME, "defaultvalue");
                    Toast.makeText(MainActivity.this, "Added an account: " + name, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Added an account:" + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void requestSync() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType(getResources().getString(R.string.account_type));

        Log.d(TAG, "Name: " + accounts[0].name + " Type: " + accounts[0].type);
        Toast.makeText(MainActivity.this, "Get Accounts: " + accounts.length, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        final Account account = new Account(accounts[0].name, getResources().getString(R.string.account_type));

        ContentResolver.requestSync(account, getResources().getString(R.string.content_authority), bundle);
    }

    private void displayQuery(Cursor cursor) {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(MyTable.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(MyTable.COLUMN_NAME));
            String sex = cursor.getString(cursor.getColumnIndex(MyTable.COLUMN_SEX));
            showMessage("_id: " + id + " _name: " + name + " _sex:" + sex);
            cursor.moveToNext();
        }
    }

    private Cursor query() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(MyContentProvider.CONTENT_URI, MyTable.ALL_PROJECTION, null, null, null);
        return cursor;
    }

    private void insertRandomFive() {
        ContentResolver cr = getContentResolver();
        for (int i = 0; i < 5; i++) {
            ContentValues cv = new ContentValues();
            Random r = new Random();
            cv.put(MyTable.COLUMN_NAME, nameList[(r.nextInt(nameList.length))]);
            cv.put(MyTable.COLUMN_SEX, sexList[(r.nextInt(sexList.length))]);
            cr.insert(MyContentProvider.CONTENT_URI, cv);
        }
    }

    private int recreateDb() {
        ContentResolver cv = getContentResolver();
        Uri toBeSubmitted = Uri.parse(MyContentProvider.CONTENT_URI + "/updateDb");
        int result = cv.update(toBeSubmitted, null, null, null);
        return result;
    }

    private void showMessage(String message) {
        data.append("\n");
        data.append(message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        addAccount.setOnClickListener(null);
        requestSync.setOnClickListener(null);
        queryAll.setOnClickListener(null);
        random.setOnClickListener(null);
        recreate.setOnClickListener(null);
    }
}
