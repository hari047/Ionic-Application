package org.apache.cordova.facebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialogException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.FacebookServiceException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.Callback;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.facebook.applinks.AppLinkData.CompletionHandler;
import com.facebook.internal.NativeProtocol;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer.Result;
import com.facebook.share.internal.ShareConstants;
import com.facebook.share.model.GameRequestContent.ActionType;
import com.facebook.share.model.GameRequestContent.Builder;
import com.facebook.share.model.GameRequestContent.Filters;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectPlugin extends CordovaPlugin {
    private static final int INVALID_ERROR_CODE = -2;
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final Set<String> OTHER_PUBLISH_PERMISSIONS = new HashSet<String>() {
        {
            add("ads_management");
            add("create_event");
            add("rsvp_event");
        }
    };
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private final String TAG = "ConnectPlugin";
    private CallbackManager callbackManager;
    private GameRequestDialog gameRequestDialog;
    /* access modifiers changed from: private */
    public String graphPath;
    /* access modifiers changed from: private */
    public CallbackContext lastGraphContext = null;
    private AppEventsLogger logger;
    /* access modifiers changed from: private */
    public CallbackContext loginContext = null;
    private MessageDialog messageDialog;
    private ShareDialog shareDialog;
    /* access modifiers changed from: private */
    public CallbackContext showDialogContext = null;

    /* access modifiers changed from: protected */
    public void pluginInitialize() {
        FacebookSdk.sdkInitialize(this.cordova.getActivity().getApplicationContext());
        this.callbackManager = Factory.create();
        this.logger = AppEventsLogger.newLogger(this.cordova.getActivity().getApplicationContext());
        this.cordova.setActivityResultCallback(this);
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphJSONObjectCallback() {
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                        if (response.getError() != null) {
                            if (ConnectPlugin.this.lastGraphContext != null) {
                                ConnectPlugin.this.lastGraphContext.error(ConnectPlugin.this.getFacebookRequestErrorResponse(response.getError()));
                            } else if (ConnectPlugin.this.loginContext != null) {
                                ConnectPlugin.this.loginContext.error(ConnectPlugin.this.getFacebookRequestErrorResponse(response.getError()));
                            }
                        } else if (ConnectPlugin.this.lastGraphContext != null) {
                            ConnectPlugin.this.makeGraphCall(ConnectPlugin.this.lastGraphContext);
                        } else if (ConnectPlugin.this.loginContext != null) {
                            Log.d("ConnectPlugin", "returning login object " + jsonObject.toString());
                            ConnectPlugin.this.loginContext.success(ConnectPlugin.this.getResponse());
                            ConnectPlugin.this.loginContext = null;
                        }
                    }
                }).executeAsync();
            }

            public void onCancel() {
                ConnectPlugin.this.handleError(new FacebookOperationCanceledException(), ConnectPlugin.this.loginContext);
            }

            public void onError(FacebookException e) {
                Log.e("Activity", String.format("Error: %s", new Object[]{e.toString()}));
                ConnectPlugin.this.handleError(e, ConnectPlugin.this.loginContext);
                if ((e instanceof FacebookAuthorizationException) && AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                }
            }
        });
        this.shareDialog = new ShareDialog(this.cordova.getActivity());
        this.shareDialog.registerCallback(this.callbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                if (ConnectPlugin.this.showDialogContext != null) {
                    ConnectPlugin.this.showDialogContext.success(result.getPostId());
                    ConnectPlugin.this.showDialogContext = null;
                }
            }

            public void onCancel() {
                ConnectPlugin.this.handleError(new FacebookOperationCanceledException(), ConnectPlugin.this.showDialogContext);
            }

            public void onError(FacebookException e) {
                Log.e("Activity", String.format("Error: %s", new Object[]{e.toString()}));
                ConnectPlugin.this.handleError(e, ConnectPlugin.this.showDialogContext);
            }
        });
        this.messageDialog = new MessageDialog(this.cordova.getActivity());
        this.messageDialog.registerCallback(this.callbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                if (ConnectPlugin.this.showDialogContext != null) {
                    ConnectPlugin.this.showDialogContext.success();
                    ConnectPlugin.this.showDialogContext = null;
                }
            }

            public void onCancel() {
                ConnectPlugin.this.handleError(new FacebookOperationCanceledException(), ConnectPlugin.this.showDialogContext);
            }

            public void onError(FacebookException e) {
                Log.e("Activity", String.format("Error: %s", new Object[]{e.toString()}));
                ConnectPlugin.this.handleError(e, ConnectPlugin.this.showDialogContext);
            }
        });
        this.gameRequestDialog = new GameRequestDialog(this.cordova.getActivity());
        this.gameRequestDialog.registerCallback(this.callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                if (ConnectPlugin.this.showDialogContext != null) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("requestId", result.getRequestId());
                        json.put("recipientsIds", new JSONArray(result.getRequestRecipients()));
                        ConnectPlugin.this.showDialogContext.success(json);
                        ConnectPlugin.this.showDialogContext = null;
                    } catch (JSONException e) {
                        ConnectPlugin.this.showDialogContext.success();
                        ConnectPlugin.this.showDialogContext = null;
                    }
                }
            }

            public void onCancel() {
                ConnectPlugin.this.handleError(new FacebookOperationCanceledException(), ConnectPlugin.this.showDialogContext);
            }

            public void onError(FacebookException e) {
                Log.e("Activity", String.format("Error: %s", new Object[]{e.toString()}));
                ConnectPlugin.this.handleError(e, ConnectPlugin.this.showDialogContext);
            }
        });
    }

    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        AppEventsLogger.activateApp((Context) this.cordova.getActivity());
    }

    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        AppEventsLogger.deactivateApp(this.cordova.getActivity());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("ConnectPlugin", "activity result in plugin: requestCode(" + requestCode + "), resultCode(" + resultCode + ")");
        this.callbackManager.onActivityResult(requestCode, resultCode, intent);
    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("login")) {
            executeLogin(args, callbackContext);
            return true;
        } else if (action.equals("logout")) {
            if (hasAccessToken()) {
                LoginManager.getInstance().logOut();
                callbackContext.success();
                return true;
            }
            callbackContext.error("No valid session found, must call init and login before logout.");
            return true;
        } else if (action.equals("getLoginStatus")) {
            callbackContext.success(getResponse());
            return true;
        } else if (action.equals("getAccessToken")) {
            if (hasAccessToken()) {
                callbackContext.success(AccessToken.getCurrentAccessToken().getToken());
                return true;
            }
            callbackContext.error("Session not open.");
            return true;
        } else if (action.equals("logEvent")) {
            executeLogEvent(args, callbackContext);
            return true;
        } else if (action.equals("logPurchase")) {
            if (args.length() != 2) {
                callbackContext.error("Invalid arguments");
                return true;
            }
            this.logger.logPurchase(new BigDecimal(args.getString(0)), Currency.getInstance(args.getString(1)));
            callbackContext.success();
            return true;
        } else if (action.equals("showDialog")) {
            executeDialog(args, callbackContext);
            return true;
        } else if (action.equals("graphApi")) {
            executeGraph(args, callbackContext);
            return true;
        } else if (action.equals("getDeferredApplink")) {
            executeGetDeferredApplink(args, callbackContext);
            return true;
        } else if (!action.equals("activateApp")) {
            return false;
        } else {
            this.cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    AppEventsLogger.activateApp((Context) ConnectPlugin.this.cordova.getActivity());
                }
            });
            return true;
        }
    }

    private void executeGetDeferredApplink(JSONArray args, final CallbackContext callbackContext) {
        AppLinkData.fetchDeferredAppLinkData(this.cordova.getActivity().getApplicationContext(), new CompletionHandler() {
            public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                PluginResult pr;
                if (appLinkData == null) {
                    pr = new PluginResult(Status.OK, "");
                } else {
                    pr = new PluginResult(Status.OK, appLinkData.getTargetUri().toString());
                }
                callbackContext.sendPluginResult(pr);
            }
        });
    }

    private void executeDialog(JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject parameters;
        HashMap hashMap = new HashMap();
        String method = null;
        try {
            parameters = args.getJSONObject(0);
        } catch (JSONException e) {
            parameters = new JSONObject();
        }
        Iterator<String> iter = parameters.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            if (key.equals("method")) {
                try {
                    method = parameters.getString(key);
                } catch (JSONException e2) {
                    Log.w("ConnectPlugin", "Nonstring method parameter provided to dialog");
                }
            } else {
                try {
                    hashMap.put(key, parameters.getString(key));
                } catch (JSONException e3) {
                    Log.w("ConnectPlugin", "Non-string parameter provided to dialog discarded");
                }
            }
        }
        if (method == null) {
            callbackContext.error("No method provided");
        } else if (method.equalsIgnoreCase("apprequests")) {
            if (!GameRequestDialog.canShow()) {
                callbackContext.error("Cannot show dialog");
                return;
            }
            this.showDialogContext = callbackContext;
            PluginResult pluginResult = new PluginResult(Status.NO_RESULT);
            pluginResult.setKeepCallback(true);
            this.showDialogContext.sendPluginResult(pluginResult);
            Builder builder = new Builder();
            if (hashMap.containsKey(ShareConstants.WEB_DIALOG_PARAM_MESSAGE)) {
                builder.setMessage((String) hashMap.get(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
            }
            if (hashMap.containsKey("to")) {
                builder.setTo((String) hashMap.get("to"));
            }
            if (hashMap.containsKey(ShareConstants.WEB_DIALOG_PARAM_DATA)) {
                builder.setData((String) hashMap.get(ShareConstants.WEB_DIALOG_PARAM_DATA));
            }
            if (hashMap.containsKey("title")) {
                builder.setTitle((String) hashMap.get("title"));
            }
            if (hashMap.containsKey("objectId")) {
                builder.setObjectId((String) hashMap.get("objectId"));
            }
            if (hashMap.containsKey("actionType")) {
                try {
                    builder.setActionType(ActionType.valueOf((String) hashMap.get("actionType")));
                } catch (IllegalArgumentException e4) {
                    Log.w("ConnectPlugin", "Discarding invalid argument actionType");
                }
            }
            if (hashMap.containsKey(ShareConstants.WEB_DIALOG_PARAM_FILTERS)) {
                try {
                    builder.setFilters(Filters.valueOf((String) hashMap.get(ShareConstants.WEB_DIALOG_PARAM_FILTERS)));
                } catch (IllegalArgumentException e5) {
                    Log.w("ConnectPlugin", "Discarding invalid argument filters");
                }
            }
            this.cordova.setActivityResultCallback(this);
            this.gameRequestDialog.show(builder.build());
        } else if (method.equalsIgnoreCase(ShareDialog.WEB_SHARE_DIALOG) || method.equalsIgnoreCase("feed")) {
            if (!ShareDialog.canShow(ShareLinkContent.class)) {
                callbackContext.error("Cannot show dialog");
                return;
            }
            this.showDialogContext = callbackContext;
            PluginResult pluginResult2 = new PluginResult(Status.NO_RESULT);
            pluginResult2.setKeepCallback(true);
            this.showDialogContext.sendPluginResult(pluginResult2);
            ShareLinkContent content = buildContent(hashMap);
            this.cordova.setActivityResultCallback(this);
            this.shareDialog.show(content);
        } else if (method.equalsIgnoreCase("share_open_graph")) {
            if (!ShareDialog.canShow(ShareOpenGraphContent.class)) {
                callbackContext.error("Cannot show dialog");
                return;
            }
            this.showDialogContext = callbackContext;
            PluginResult pluginResult3 = new PluginResult(Status.NO_RESULT);
            pluginResult3.setKeepCallback(true);
            this.showDialogContext.sendPluginResult(pluginResult3);
            if (!hashMap.containsKey(NativeProtocol.WEB_DIALOG_ACTION)) {
                callbackContext.error("Missing required parameter 'action'");
            }
            if (!hashMap.containsKey("object")) {
                callbackContext.error("Missing required parameter 'object'.");
            }
            ShareOpenGraphObject.Builder objectBuilder = new ShareOpenGraphObject.Builder();
            JSONObject jObject = new JSONObject((String) hashMap.get("object"));
            Iterator<?> objectKeys = jObject.keys();
            String objectType = "";
            while (objectKeys.hasNext()) {
                String key2 = (String) objectKeys.next();
                String value = jObject.getString(key2);
                objectBuilder.putString(key2, value);
                if (key2.equals("og:type")) {
                    objectType = value;
                }
            }
            if (objectType.equals("")) {
                callbackContext.error("Missing required object parameter 'og:type'");
            }
            ShareOpenGraphAction.Builder actionBuilder = new ShareOpenGraphAction.Builder();
            actionBuilder.setActionType((String) hashMap.get(NativeProtocol.WEB_DIALOG_ACTION));
            if (hashMap.containsKey(ShareConstants.WEB_DIALOG_PARAM_ACTION_PROPERTIES)) {
                JSONObject jActionProperties = new JSONObject((String) hashMap.get(ShareConstants.WEB_DIALOG_PARAM_ACTION_PROPERTIES));
                Iterator<?> actionKeys = jActionProperties.keys();
                while (actionKeys.hasNext()) {
                    String actionKey = (String) actionKeys.next();
                    actionBuilder.putString(actionKey, jActionProperties.getString(actionKey));
                }
            }
            actionBuilder.putObject(objectType, objectBuilder.build());
            this.shareDialog.show(new ShareOpenGraphContent.Builder().setPreviewPropertyName(objectType).setAction(actionBuilder.build()).build());
        } else if (!method.equalsIgnoreCase("send")) {
            callbackContext.error("Unsupported dialog method.");
        } else if (!MessageDialog.canShow(ShareLinkContent.class)) {
            callbackContext.error("Cannot show dialog");
        } else {
            this.showDialogContext = callbackContext;
            PluginResult pluginResult4 = new PluginResult(Status.NO_RESULT);
            pluginResult4.setKeepCallback(true);
            this.showDialogContext.sendPluginResult(pluginResult4);
            ShareLinkContent.Builder builder2 = new ShareLinkContent.Builder();
            if (hashMap.containsKey("link")) {
                builder2.setContentUrl(Uri.parse((String) hashMap.get("link")));
            }
            if (hashMap.containsKey(ShareConstants.FEED_CAPTION_PARAM)) {
                builder2.setContentTitle((String) hashMap.get(ShareConstants.FEED_CAPTION_PARAM));
            }
            if (hashMap.containsKey("picture")) {
                builder2.setImageUrl(Uri.parse((String) hashMap.get("picture")));
            }
            if (hashMap.containsKey("description")) {
                builder2.setContentDescription((String) hashMap.get("description"));
            }
            this.messageDialog.show(builder2.build());
        }
    }

    private void executeGraph(JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.lastGraphContext = callbackContext;
        CallbackContext graphContext = callbackContext;
        PluginResult pr = new PluginResult(Status.NO_RESULT);
        pr.setKeepCallback(true);
        graphContext.sendPluginResult(pr);
        this.graphPath = args.getString(0);
        JSONArray arr = args.getJSONArray(1);
        Set<String> permissions = new HashSet<>(arr.length());
        for (int i = 0; i < arr.length(); i++) {
            permissions.add(arr.getString(i));
        }
        if (permissions.size() == 0) {
            makeGraphCall(graphContext);
            return;
        }
        boolean publishPermissions = false;
        boolean readPermissions = false;
        String declinedPermission = null;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken.getPermissions().containsAll(permissions)) {
            makeGraphCall(graphContext);
            return;
        }
        Set<String> declined = accessToken.getDeclinedPermissions();
        Iterator it = permissions.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String permission = (String) it.next();
            if (!declined.contains(permission)) {
                if (isPublishPermission(permission)) {
                    publishPermissions = true;
                } else {
                    readPermissions = true;
                }
                if (publishPermissions && readPermissions) {
                    break;
                }
            } else {
                declinedPermission = permission;
                break;
            }
        }
        if (declinedPermission != null) {
            graphContext.error("This request needs declined permission: " + declinedPermission);
        } else if (!publishPermissions || !readPermissions) {
            this.cordova.setActivityResultCallback(this);
            LoginManager loginManager = LoginManager.getInstance();
            if (publishPermissions) {
                loginManager.logInWithPublishPermissions(this.cordova.getActivity(), (Collection<String>) permissions);
            } else {
                loginManager.logInWithReadPermissions(this.cordova.getActivity(), (Collection<String>) permissions);
            }
        } else {
            graphContext.error("Cannot ask for both read and publish permissions.");
        }
    }

    private void executeLogEvent(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (args.length() == 0) {
            callbackContext.error("Invalid arguments");
            return;
        }
        String eventName = args.getString(0);
        if (args.length() == 1) {
            this.logger.logEvent(eventName);
            callbackContext.success();
            return;
        }
        JSONObject params = args.getJSONObject(1);
        Bundle parameters = new Bundle();
        Iterator<String> iter = params.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            try {
                parameters.putString(key, params.getString(key));
            } catch (JSONException e) {
                Log.w("ConnectPlugin", "Type in AppEvent parameters was not String for key: " + key);
                try {
                    parameters.putInt(key, params.getInt(key));
                } catch (JSONException e2) {
                    Log.e("ConnectPlugin", "Unsupported type in AppEvent parameters for key: " + key);
                }
            }
        }
        if (args.length() == 2) {
            this.logger.logEvent(eventName, parameters);
            callbackContext.success();
        }
        if (args.length() == 3) {
            this.logger.logEvent(eventName, args.getDouble(2), parameters);
            callbackContext.success();
        }
    }

    private void executeLogin(JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("ConnectPlugin", "login FB");
        this.lastGraphContext = null;
        Set<String> permissions = new HashSet<>(args.length());
        for (int i = 0; i < args.length(); i++) {
            permissions.add(args.getString(i));
        }
        this.loginContext = callbackContext;
        PluginResult pr = new PluginResult(Status.NO_RESULT);
        pr.setKeepCallback(true);
        this.loginContext.sendPluginResult(pr);
        if (!hasAccessToken()) {
            this.cordova.setActivityResultCallback(this);
            LoginManager.getInstance().logInWithReadPermissions(this.cordova.getActivity(), (Collection<String>) permissions);
            return;
        }
        boolean publishPermissions = false;
        boolean readPermissions = false;
        if (permissions.size() == 0) {
            readPermissions = true;
        }
        for (String permission : permissions) {
            if (isPublishPermission(permission)) {
                publishPermissions = true;
            } else {
                readPermissions = true;
            }
            if (publishPermissions && readPermissions) {
                break;
            }
        }
        if (!publishPermissions || !readPermissions) {
            this.cordova.setActivityResultCallback(this);
            if (publishPermissions) {
                LoginManager.getInstance().logInWithPublishPermissions(this.cordova.getActivity(), (Collection<String>) permissions);
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this.cordova.getActivity(), (Collection<String>) permissions);
            }
        } else {
            this.loginContext.error("Cannot ask for both read and publish permissions.");
            this.loginContext = null;
        }
    }

    private ShareLinkContent buildContent(Map<String, String> paramBundle) {
        ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
        if (paramBundle.containsKey(ShareConstants.WEB_DIALOG_PARAM_HREF)) {
            builder.setContentUrl(Uri.parse((String) paramBundle.get(ShareConstants.WEB_DIALOG_PARAM_HREF)));
        }
        if (paramBundle.containsKey(ShareConstants.FEED_CAPTION_PARAM)) {
            builder.setContentTitle((String) paramBundle.get(ShareConstants.FEED_CAPTION_PARAM));
        }
        if (paramBundle.containsKey("description")) {
            builder.setContentDescription((String) paramBundle.get("description"));
        }
        if (paramBundle.containsKey("link")) {
            builder.setContentUrl(Uri.parse((String) paramBundle.get("link")));
        }
        if (paramBundle.containsKey("picture")) {
            builder.setImageUrl(Uri.parse((String) paramBundle.get("picture")));
        }
        if (paramBundle.containsKey(ShareConstants.WEB_DIALOG_PARAM_QUOTE)) {
            builder.setQuote((String) paramBundle.get(ShareConstants.WEB_DIALOG_PARAM_QUOTE));
        }
        if (paramBundle.containsKey(ShareConstants.WEB_DIALOG_PARAM_HASHTAG)) {
            builder.setShareHashtag(new ShareHashtag.Builder().setHashtag((String) paramBundle.get(ShareConstants.WEB_DIALOG_PARAM_HASHTAG)).build());
        }
        return builder.build();
    }

    private boolean hasAccessToken() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    /* access modifiers changed from: private */
    public void handleError(FacebookException exception, CallbackContext context) {
        if (exception.getMessage() != null) {
            Log.e("ConnectPlugin", exception.toString());
        }
        String errMsg = "Facebook error: " + exception.getMessage();
        int errorCode = -2;
        if (exception instanceof FacebookOperationCanceledException) {
            errMsg = "User cancelled dialog";
            errorCode = 4201;
        } else if (exception instanceof FacebookDialogException) {
            errMsg = "Dialog error: " + exception.getMessage();
        }
        if (context != null) {
            context.error(getErrorResponse(exception, errMsg, errorCode));
        } else {
            Log.e("ConnectPlugin", "Error already sent so no context, msg: " + errMsg + ", code: " + errorCode);
        }
    }

    /* access modifiers changed from: private */
    public void makeGraphCall(final CallbackContext graphContext) {
        String[] queries;
        try {
            this.graphPath = URLDecoder.decode(this.graphPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] urlParts = this.graphPath.split("\\?");
        GraphRequest graphRequest = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), urlParts[0], new Callback() {
            public void onCompleted(GraphResponse response) {
                if (graphContext != null) {
                    if (response.getError() != null) {
                        graphContext.error(ConnectPlugin.this.getFacebookRequestErrorResponse(response.getError()));
                    } else {
                        graphContext.success(response.getJSONObject());
                    }
                    ConnectPlugin.this.graphPath = null;
                }
            }
        });
        Bundle params = graphRequest.getParameters();
        if (urlParts.length > 1) {
            for (String query : urlParts[1].split("&")) {
                int splitPoint = query.indexOf("=");
                if (splitPoint > 0) {
                    params.putString(query.substring(0, splitPoint), query.substring(splitPoint + 1, query.length()));
                }
            }
        }
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    private boolean isPublishPermission(String permission) {
        return permission != null && (permission.startsWith(PUBLISH_PERMISSION_PREFIX) || permission.startsWith(MANAGE_PERMISSION_PREFIX) || OTHER_PUBLISH_PERMISSIONS.contains(permission));
    }

    public JSONObject getResponse() {
        String response;
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (hasAccessToken()) {
            response = "{\"status\": \"connected\",\"authResponse\": {\"accessToken\": \"" + accessToken.getToken() + "\",\"expiresIn\": \"" + Math.max((accessToken.getExpires().getTime() - new Date().getTime()) / 1000, 0) + "\",\"session_key\": true,\"sig\": \"...\",\"userID\": \"" + accessToken.getUserId() + "\"}}";
        } else {
            response = "{\"status\": \"unknown\"}";
        }
        try {
            return new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject getFacebookRequestErrorResponse(FacebookRequestError error) {
        String response = "{\"errorCode\": \"" + error.getErrorCode() + "\",\"errorType\": \"" + error.getErrorType() + "\",\"errorMessage\": \"" + error.getErrorMessage() + "\"";
        if (error.getErrorUserMessage() != null) {
            response = response + ",\"errorUserMessage\": \"" + error.getErrorUserMessage() + "\"";
        }
        if (error.getErrorUserTitle() != null) {
            response = response + ",\"errorUserTitle\": \"" + error.getErrorUserTitle() + "\"";
        }
        try {
            return new JSONObject(response + "}");
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    public JSONObject getErrorResponse(Exception error, String message, int errorCode) {
        if (error instanceof FacebookServiceException) {
            return getFacebookRequestErrorResponse(((FacebookServiceException) error).getRequestError());
        }
        String response = "{";
        if (error instanceof FacebookDialogException) {
            errorCode = ((FacebookDialogException) error).getErrorCode();
        }
        if (errorCode != -2) {
            response = response + "\"errorCode\": \"" + errorCode + "\",";
        }
        if (message == null) {
            message = error.getMessage();
        }
        try {
            return new JSONObject(response + "\"errorMessage\": \"" + message + "\"}");
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    private static Object wrapObject(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if ((o instanceof JSONArray) || (o instanceof JSONObject) || o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            }
            if (o.getClass().isArray()) {
                return new JSONArray(o);
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if ((o instanceof Boolean) || (o instanceof Byte) || (o instanceof Character) || (o instanceof Double) || (o instanceof Float) || (o instanceof Integer) || (o instanceof Long) || (o instanceof Short) || (o instanceof String)) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
            return null;
        } catch (Exception e) {
        }
    }
}
