package com.codespurt.syncadapter.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.codespurt.syncadapter.R;

/**
 * Created by CodeSpurt on 05-08-2017.
 */

public class Authenticator extends AbstractAccountAuthenticator {

    private static final String TAG = Authenticator.class.getSimpleName();

    private static Context mContext;

    public Authenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        Log.d(TAG, "editProperties");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String s, String s1, String[] strings, Bundle bundle) throws NetworkErrorException {
        Log.d(TAG, "addAccount");

        String name = "testing@gmail.com";
        String password = "11111111";
        String authToken = "22222222";

        final Account account = new Account(name, mContext.getResources().getString(R.string.account_type));
        AccountManager manager = AccountManager.get(mContext);
        manager.addAccountExplicitly(account, password, null);
        manager.setAuthToken(account, mContext.getResources().getString(R.string.account_auth_token_type), authToken);

        Bundle b = new Bundle();
        b.putString(AccountManager.KEY_ACCOUNT_NAME, name);
        return b;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        Log.d(TAG, "confirmcredentials");
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        Log.d(TAG, "getAuthToken");
        final AccountManager manager = AccountManager.get(mContext);
        final Bundle b = new Bundle();
        String authToken = manager.peekAuthToken(account, mContext.getResources().getString(R.string.account_auth_token_type));
        b.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        b.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        b.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        return b;
    }

    @Override
    public String getAuthTokenLabel(String s) {
        Log.d(TAG, "getAuthTokenLabel");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        Log.d(TAG, "updateCredentials");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        Log.d(TAG, "hasFeatures");
        throw new UnsupportedOperationException();
    }
}