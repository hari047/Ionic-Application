package com.facebook.marketing.internal;

import android.content.Context;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public final class MarketingLogger {
    static final String ACTIVITY_NAME = "_activity_name";
    static final String CODELESS_ACTION_GESTURE_TRIGGERED = "gesture_triggered";
    static final String CODELESS_ACTION_INDEXING_CANCELLED = "indexing_cancelled";
    static final String CODELESS_ACTION_INDEXING_COMPLETE = "indexing_complete";
    static final String CODELESS_ACTION_INDEXING_START = "indexing_start";
    static final String CODELESS_ACTION_KEY = "_codeless_action";
    static final String CODELESS_ACTION_SDK_INITIALIZED = "sdk_initialized";
    static final String CODELESS_ACTION_SESSION_READY = "session_ready";
    static final String EVENT_NAME_CODELESS_DEBUG = "fb_codeless_debug";
    private final AppEventsLogger appEventsLogger;

    public MarketingLogger(Context context, String applicationId) {
        this.appEventsLogger = AppEventsLogger.newLogger(context, applicationId);
    }

    public void logCodelessInitialized() {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_SDK_INITIALIZED);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }

    public void logGestureTriggered() {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_GESTURE_TRIGGERED);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }

    public void logSessionReady() {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_SESSION_READY);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }

    public void logIndexingStart(String activityName) {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_INDEXING_START);
            params.putString(ACTIVITY_NAME, activityName);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }

    public void logIndexingComplete(String activityName) {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_INDEXING_COMPLETE);
            params.putString(ACTIVITY_NAME, activityName);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }

    public void logIndexingCancelled(String activityName) {
        if (FacebookSdk.getAutoLogAppEventsEnabled()) {
            Bundle params = new Bundle();
            params.putString(CODELESS_ACTION_KEY, CODELESS_ACTION_INDEXING_CANCELLED);
            params.putString(ACTIVITY_NAME, activityName);
            this.appEventsLogger.logSdkEvent(EVENT_NAME_CODELESS_DEBUG, null, params);
        }
    }
}
