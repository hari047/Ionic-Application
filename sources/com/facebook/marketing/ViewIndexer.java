package com.facebook.marketing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.codeless.internal.ViewHierarchy;
import com.facebook.internal.Logger;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.marketing.internal.Constants;
import com.facebook.marketing.internal.MarketingLogger;
import com.facebook.marketing.internal.MarketingUtils;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewIndexer {
    private static final String APP_VERSION_PARAM = "app_version";
    private static final String PLATFORM_PARAM = "platform";
    private static final String SUCCESS = "success";
    /* access modifiers changed from: private */
    public static final String TAG = ViewIndexer.class.getCanonicalName();
    private static final String TREE_PARAM = "tree";
    /* access modifiers changed from: private */
    public Activity activity;
    /* access modifiers changed from: private */
    public Timer indexingTimer;
    /* access modifiers changed from: private */
    public final MarketingLogger logger = new MarketingLogger(FacebookSdk.getApplicationContext(), FacebookSdk.getApplicationId());
    /* access modifiers changed from: private */
    public String previousDigest = null;
    /* access modifiers changed from: private */
    public final Handler uiThreadHandler = new Handler(Looper.getMainLooper());

    private static class ScreenshotTaker implements Callable<String> {
        private WeakReference<View> rootView;

        public ScreenshotTaker(View rootView2) {
            this.rootView = new WeakReference<>(rootView2);
        }

        public String call() throws Exception {
            View view = (View) this.rootView.get();
            if (view == null || view.getWidth() == 0 || view.getHeight() == 0) {
                return "";
            }
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.RGB_565);
            view.draw(new Canvas(bitmap));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 10, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), 2);
        }
    }

    public ViewIndexer(Activity activity2) {
        this.activity = activity2;
    }

    public void schedule() {
        final String activityName = this.activity.getClass().getSimpleName();
        String applicationId = FacebookSdk.getApplicationId();
        final TimerTask indexingTask = new TimerTask() {
            public void run() {
                String screenshot;
                try {
                    View rootView = ViewIndexer.this.activity.getWindow().getDecorView().getRootView();
                    if (CodelessActivityLifecycleTracker.getIsAppIndexingEnabled()) {
                        FutureTask<String> screenshotFuture = new FutureTask<>(new ScreenshotTaker(rootView));
                        ViewIndexer.this.uiThreadHandler.post(screenshotFuture);
                        String str = "";
                        try {
                            screenshot = (String) screenshotFuture.get(1, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            Log.e(ViewIndexer.TAG, "Failed to take screenshot.", e);
                        }
                        JSONObject viewTree = new JSONObject();
                        try {
                            viewTree.put("screenname", activityName);
                            viewTree.put("screenshot", screenshot);
                            JSONArray viewArray = new JSONArray();
                            viewArray.put(ViewHierarchy.getDictionaryOfView(rootView));
                            viewTree.put("view", viewArray);
                        } catch (JSONException e2) {
                            Log.e(ViewIndexer.TAG, "Failed to create JSONObject");
                        }
                        ViewIndexer.this.sendToServer(viewTree.toString());
                    }
                } catch (Exception e3) {
                    Log.e(ViewIndexer.TAG, "UI Component tree indexing failure!", e3);
                }
            }
        };
        FacebookSdk.getExecutor().execute(new Runnable() {
            public void run() {
                try {
                    if (ViewIndexer.this.indexingTimer != null) {
                        ViewIndexer.this.indexingTimer.cancel();
                    }
                    ViewIndexer.this.previousDigest = null;
                    ViewIndexer.this.indexingTimer = new Timer();
                    ViewIndexer.this.indexingTimer.scheduleAtFixedRate(indexingTask, 0, 1000);
                } catch (Exception e) {
                    Log.e(ViewIndexer.TAG, "Error scheduling indexing job", e);
                }
            }
        });
    }

    public void unschedule() {
        if (this.indexingTimer != null) {
            try {
                this.indexingTimer.cancel();
                this.indexingTimer = null;
                if (CodelessActivityLifecycleTracker.getIsAppIndexingEnabled()) {
                    this.logger.logIndexingCancelled(this.activity.getClass().getCanonicalName());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error unscheduling indexing job", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void sendToServer(final String tree) {
        FacebookSdk.getExecutor().execute(new Runnable() {
            public void run() {
                String appId = FacebookSdk.getApplicationId();
                String activityName = ViewIndexer.this.activity.getClass().getCanonicalName();
                String currentDigest = Utility.md5hash(tree);
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (currentDigest == null || !currentDigest.equals(ViewIndexer.this.previousDigest)) {
                    ViewIndexer.this.logger.logIndexingStart(activityName);
                    GraphRequest request = ViewIndexer.buildAppIndexingRequest(tree, accessToken, appId);
                    if (request != null) {
                        GraphResponse res = request.executeAndWait();
                        try {
                            JSONObject jsonRes = res.getJSONObject();
                            if (jsonRes != null) {
                                if (jsonRes.has("success") && jsonRes.getString("success") == ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) {
                                    Logger.log(LoggingBehavior.APP_EVENTS, ViewIndexer.TAG, "Successfully send UI component tree to server");
                                    ViewIndexer.this.previousDigest = currentDigest;
                                    ViewIndexer.this.logger.logIndexingComplete(activityName);
                                }
                                if (jsonRes.has(Constants.APP_INDEXING_ENABLED)) {
                                    CodelessActivityLifecycleTracker.updateAppIndexing(Boolean.valueOf(jsonRes.getBoolean(Constants.APP_INDEXING_ENABLED)));
                                    return;
                                }
                                return;
                            }
                            Log.e(ViewIndexer.TAG, "Error sending UI component tree to Facebook: " + res.getError());
                        } catch (JSONException e) {
                            Log.e(ViewIndexer.TAG, "Error decoding server response.", e);
                        }
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    @Nullable
    public static GraphRequest buildAppIndexingRequest(String appIndex, AccessToken accessToken, String appId) {
        GraphRequest postRequest = null;
        if (appIndex != null) {
            postRequest = GraphRequest.newPostRequest(accessToken, String.format(Locale.US, "%s/app_indexing", new Object[]{appId}), null, null);
            Bundle requestParameters = postRequest.getParameters();
            if (requestParameters == null) {
                requestParameters = new Bundle();
            }
            requestParameters.putString(TREE_PARAM, appIndex);
            requestParameters.putString(APP_VERSION_PARAM, MarketingUtils.getAppVersion());
            requestParameters.putString(PLATFORM_PARAM, Constants.PLATFORM);
            requestParameters.putString(Constants.DEVICE_SESSION_ID, CodelessActivityLifecycleTracker.getCurrentDeviceSessionID());
            postRequest.setParameters(requestParameters);
            postRequest.setCallback(new Callback() {
                public void onCompleted(GraphResponse response) {
                    Logger.log(LoggingBehavior.APP_EVENTS, ViewIndexer.TAG, "App index sent to FB!");
                }
            });
        }
        return postRequest;
    }
}
