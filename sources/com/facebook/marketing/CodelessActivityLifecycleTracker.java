package com.facebook.marketing;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.FetchedAppSettings;
import com.facebook.internal.FetchedAppSettingsManager;
import com.facebook.internal.Utility;
import com.facebook.marketing.ViewIndexingTrigger.OnShakeListener;
import com.facebook.marketing.internal.Constants;
import com.facebook.marketing.internal.MarketingLogger;
import com.facebook.marketing.internal.MarketingUtils;
import java.util.Locale;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class CodelessActivityLifecycleTracker {
    private static final String TAG = CodelessActivityLifecycleTracker.class.getCanonicalName();
    /* access modifiers changed from: private */
    public static String deviceSessionID = null;
    /* access modifiers changed from: private */
    public static Boolean isAppIndexingEnabled = Boolean.valueOf(false);
    /* access modifiers changed from: private */
    public static volatile Boolean isCheckingSession = Boolean.valueOf(false);
    /* access modifiers changed from: private */
    public static SensorManager sensorManager;
    /* access modifiers changed from: private */
    public static ViewIndexer viewIndexer;
    /* access modifiers changed from: private */
    public static final ViewIndexingTrigger viewIndexingTrigger = new ViewIndexingTrigger();

    public static void startTracking(Application application, String appId) {
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
                final Context applicationContext = activity.getApplicationContext();
                final String appId = FacebookSdk.getApplicationId();
                final FetchedAppSettings appSettings = FetchedAppSettingsManager.getAppSettingsWithoutQuery(appId);
                CodelessActivityLifecycleTracker.sensorManager = (SensorManager) applicationContext.getSystemService("sensor");
                Sensor accelerometer = CodelessActivityLifecycleTracker.sensorManager.getDefaultSensor(1);
                CodelessActivityLifecycleTracker.viewIndexer = new ViewIndexer(activity);
                CodelessActivityLifecycleTracker.viewIndexingTrigger.setOnShakeListener(new OnShakeListener() {
                    public void onShake(int count) {
                        if (count >= 3) {
                            CodelessActivityLifecycleTracker.viewIndexingTrigger.resetCount();
                            MarketingLogger logger = new MarketingLogger(applicationContext, appId);
                            logger.logGestureTriggered();
                            if (appSettings != null && appSettings.getCodelessEventsEnabled()) {
                                CodelessActivityLifecycleTracker.checkCodelessSession(appId, logger);
                            }
                        }
                    }
                });
                CodelessActivityLifecycleTracker.sensorManager.registerListener(CodelessActivityLifecycleTracker.viewIndexingTrigger, accelerometer, 2);
                if (appSettings != null && appSettings.getCodelessEventsEnabled()) {
                    CodelessActivityLifecycleTracker.viewIndexer.schedule();
                }
            }

            public void onActivityPaused(Activity activity) {
                CodelessActivityLifecycleTracker.viewIndexer.unschedule();
                CodelessActivityLifecycleTracker.sensorManager.unregisterListener(CodelessActivityLifecycleTracker.viewIndexingTrigger);
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public static void checkCodelessSession(final String applicationId, final MarketingLogger logger) {
        if (!isCheckingSession.booleanValue()) {
            isCheckingSession = Boolean.valueOf(true);
            FacebookSdk.getExecutor().execute(new Runnable() {
                public void run() {
                    GraphRequest request = GraphRequest.newPostRequest(null, String.format(Locale.US, "%s/app_indexing_session", new Object[]{applicationId}), null, null);
                    Bundle requestParameters = request.getParameters();
                    if (requestParameters == null) {
                        requestParameters = new Bundle();
                    }
                    AttributionIdentifiers identifiers = AttributionIdentifiers.getAttributionIdentifiers(FacebookSdk.getApplicationContext());
                    JSONArray extInfoArray = new JSONArray();
                    extInfoArray.put(Build.MODEL != null ? Build.MODEL : "");
                    if (identifiers == null || identifiers.getAndroidAdvertiserId() == null) {
                        extInfoArray.put("");
                    } else {
                        extInfoArray.put(identifiers.getAndroidAdvertiserId());
                    }
                    extInfoArray.put(AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    extInfoArray.put(MarketingUtils.isEmulator() ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    Locale locale = Utility.getCurrentLocale();
                    extInfoArray.put(locale.getLanguage() + "_" + locale.getCountry());
                    String extInfo = extInfoArray.toString();
                    requestParameters.putString(Constants.DEVICE_SESSION_ID, CodelessActivityLifecycleTracker.getCurrentDeviceSessionID());
                    requestParameters.putString(Constants.EXTINFO, extInfo);
                    request.setParameters(requestParameters);
                    if (request != null) {
                        JSONObject jsonRes = request.executeAndWait().getJSONObject();
                        CodelessActivityLifecycleTracker.isAppIndexingEnabled = Boolean.valueOf(jsonRes != null && jsonRes.optBoolean(Constants.APP_INDEXING_ENABLED, false));
                        if (!CodelessActivityLifecycleTracker.isAppIndexingEnabled.booleanValue()) {
                            CodelessActivityLifecycleTracker.deviceSessionID = null;
                        } else {
                            logger.logSessionReady();
                            CodelessActivityLifecycleTracker.viewIndexer.schedule();
                        }
                    }
                    CodelessActivityLifecycleTracker.isCheckingSession = Boolean.valueOf(false);
                }
            });
        }
    }

    public static String getCurrentDeviceSessionID() {
        if (deviceSessionID == null) {
            deviceSessionID = UUID.randomUUID().toString();
        }
        return deviceSessionID;
    }

    public static boolean getIsAppIndexingEnabled() {
        return isAppIndexingEnabled.booleanValue();
    }

    public static void updateAppIndexing(Boolean appIndexingEnalbed) {
        isAppIndexingEnabled = appIndexingEnalbed;
    }
}
