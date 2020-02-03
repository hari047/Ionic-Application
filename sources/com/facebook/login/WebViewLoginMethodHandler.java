package com.facebook.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.p000v4.app.FragmentActivity;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.internal.FacebookDialogFragment;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.WebDialog;
import com.facebook.internal.WebDialog.Builder;
import com.facebook.internal.WebDialog.OnCompleteListener;
import com.facebook.login.LoginClient.Request;

class WebViewLoginMethodHandler extends WebLoginMethodHandler {
    public static final Creator<WebViewLoginMethodHandler> CREATOR = new Creator<WebViewLoginMethodHandler>() {
        public WebViewLoginMethodHandler createFromParcel(Parcel source) {
            return new WebViewLoginMethodHandler(source);
        }

        public WebViewLoginMethodHandler[] newArray(int size) {
            return new WebViewLoginMethodHandler[size];
        }
    };
    private String e2e;
    private WebDialog loginDialog;

    static class AuthDialogBuilder extends Builder {
        private static final String OAUTH_DIALOG = "oauth";
        private String authType;
        private String e2e;
        private String redirect_uri = ServerProtocol.DIALOG_REDIRECT_URI;

        public AuthDialogBuilder(Context context, String applicationId, Bundle parameters) {
            super(context, applicationId, OAUTH_DIALOG, parameters);
        }

        public AuthDialogBuilder setE2E(String e2e2) {
            this.e2e = e2e2;
            return this;
        }

        public AuthDialogBuilder setIsRerequest(boolean isRerequest) {
            return this;
        }

        public AuthDialogBuilder setIsChromeOS(boolean isChromeOS) {
            this.redirect_uri = isChromeOS ? ServerProtocol.DIALOG_REDIRECT_CHROME_OS_URI : ServerProtocol.DIALOG_REDIRECT_URI;
            return this;
        }

        public AuthDialogBuilder setAuthType(String authType2) {
            this.authType = authType2;
            return this;
        }

        public WebDialog build() {
            Bundle parameters = getParameters();
            parameters.putString(ServerProtocol.DIALOG_PARAM_REDIRECT_URI, this.redirect_uri);
            parameters.putString("client_id", getApplicationId());
            parameters.putString("e2e", this.e2e);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RESPONSE_TYPE, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN_AND_SIGNED_REQUEST);
            parameters.putString(ServerProtocol.DIALOG_PARAM_RETURN_SCOPES, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            parameters.putString(ServerProtocol.DIALOG_PARAM_AUTH_TYPE, this.authType);
            return WebDialog.newInstance(getContext(), OAUTH_DIALOG, parameters, getTheme(), getListener());
        }
    }

    WebViewLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    /* access modifiers changed from: 0000 */
    public String getNameForLogging() {
        return "web_view";
    }

    /* access modifiers changed from: 0000 */
    public AccessTokenSource getTokenSource() {
        return AccessTokenSource.WEB_VIEW;
    }

    /* access modifiers changed from: 0000 */
    public boolean needsInternetPermission() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void cancel() {
        if (this.loginDialog != null) {
            this.loginDialog.cancel();
            this.loginDialog = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean tryAuthorize(final Request request) {
        Bundle parameters = getParameters(request);
        OnCompleteListener listener = new OnCompleteListener() {
            public void onComplete(Bundle values, FacebookException error) {
                WebViewLoginMethodHandler.this.onWebDialogComplete(request, values, error);
            }
        };
        this.e2e = LoginClient.getE2E();
        addLoggingExtra("e2e", this.e2e);
        FragmentActivity fragmentActivity = this.loginClient.getActivity();
        this.loginDialog = new AuthDialogBuilder(fragmentActivity, request.getApplicationId(), parameters).setE2E(this.e2e).setIsChromeOS(Utility.isChromeOS(fragmentActivity)).setAuthType(request.getAuthType()).setOnCompleteListener(listener).build();
        FacebookDialogFragment dialogFragment = new FacebookDialogFragment();
        dialogFragment.setRetainInstance(true);
        dialogFragment.setDialog(this.loginDialog);
        dialogFragment.show(fragmentActivity.getSupportFragmentManager(), FacebookDialogFragment.TAG);
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void onWebDialogComplete(Request request, Bundle values, FacebookException error) {
        super.onComplete(request, values, error);
    }

    WebViewLoginMethodHandler(Parcel source) {
        super(source);
        this.e2e = source.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.e2e);
    }
}
