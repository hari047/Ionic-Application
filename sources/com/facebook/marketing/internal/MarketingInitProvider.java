package com.facebook.marketing.internal;

import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.FacebookSdk.InitializeCallback;
import com.facebook.marketing.CodelessActivityLifecycleTracker;

public final class MarketingInitProvider extends ContentProvider {
    private static final String TAG = MarketingInitProvider.class.getSimpleName();

    public boolean onCreate() {
        try {
            if (!FacebookSdk.isInitialized()) {
                FacebookSdk.sdkInitialize(getContext(), (InitializeCallback) new InitializeCallback() {
                    public void onInitialized() {
                        MarketingInitProvider.this.setupCodeless();
                    }
                });
            } else {
                setupCodeless();
            }
        } catch (Exception ex) {
            Log.i(TAG, "Failed to auto initialize the Marketing SDK", ex);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void setupCodeless() {
        CodelessActivityLifecycleTracker.startTracking((Application) FacebookSdk.getApplicationContext(), FacebookSdk.getApplicationId());
        new MarketingLogger((Application) FacebookSdk.getApplicationContext(), FacebookSdk.getApplicationId()).logCodelessInitialized();
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
