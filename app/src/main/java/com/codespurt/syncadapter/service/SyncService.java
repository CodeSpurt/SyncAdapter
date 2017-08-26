package com.codespurt.syncadapter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codespurt.syncadapter.adapter.SyncAdapter;

/**
 * Created by CodeSpurt on 05-08-2017.
 */

public class SyncService extends Service {

    private static SyncAdapter sTableSyncAdapter = null;
    // object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    public static final String TAG = SyncService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SyncService is created");
        // use the lock to prevent multiple access service
        synchronized (sSyncAdapterLock) {
            if (sTableSyncAdapter == null) {
                sTableSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind SyncAdapter");
        return sTableSyncAdapter.getSyncAdapterBinder();
    }
}
