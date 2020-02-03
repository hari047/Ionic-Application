package com.facebook.login;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import com.facebook.internal.AnalyticsEvents;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.ServerProtocol;
import com.facebook.login.LoginClient.Request;
import com.facebook.login.LoginClient.Result;

abstract class NativeAppLoginMethodHandler extends LoginMethodHandler {
    /* access modifiers changed from: 0000 */
    public abstract boolean tryAuthorize(Request request);

    NativeAppLoginMethodHandler(LoginClient loginClient) {
        super(loginClient);
    }

    NativeAppLoginMethodHandler(Parcel source) {
        super(source);
    }

    /* access modifiers changed from: 0000 */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Result outcome;
        Request request = this.loginClient.getPendingRequest();
        if (data == null) {
            outcome = Result.createCancelResult(request, "Operation canceled");
        } else if (resultCode == 0) {
            outcome = handleResultCancel(request, data);
        } else if (resultCode != -1) {
            outcome = Result.createErrorResult(request, "Unexpected resultCode from authorization.", null);
        } else {
            outcome = handleResultOk(request, data);
        }
        if (outcome != null) {
            this.loginClient.completeAndValidate(outcome);
        } else {
            this.loginClient.tryNextHandler();
        }
        return true;
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [com.facebook.login.LoginClient$Result, java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r7v0, types: [com.facebook.login.LoginClient$Result, java.lang.String]
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY]]
      uses: [?[OBJECT, ARRAY], com.facebook.login.LoginClient$Result, java.lang.String]
      mth insns count: 41
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.facebook.login.LoginClient.Result handleResultOk(com.facebook.login.LoginClient.Request r12, android.content.Intent r13) {
        /*
            r11 = this;
            r7 = 0
            android.os.Bundle r5 = r13.getExtras()
            java.lang.String r1 = r11.getError(r5)
            java.lang.String r8 = "error_code"
            java.lang.Object r8 = r5.get(r8)
            if (r8 == 0) goto L_0x004a
            java.lang.String r8 = "error_code"
            java.lang.Object r8 = r5.get(r8)
            java.lang.String r2 = r8.toString()
        L_0x001d:
            java.lang.String r3 = r11.getErrorMessage(r5)
            java.lang.String r8 = "e2e"
            java.lang.String r0 = r5.getString(r8)
            boolean r8 = com.facebook.internal.Utility.isNullOrEmpty(r0)
            if (r8 != 0) goto L_0x0031
            r11.logWebLoginCompleted(r0)
        L_0x0031:
            if (r1 != 0) goto L_0x0056
            if (r2 != 0) goto L_0x0056
            if (r3 != 0) goto L_0x0056
            java.util.Set r8 = r12.getPermissions()     // Catch:{ FacebookException -> 0x004c }
            com.facebook.AccessTokenSource r9 = com.facebook.AccessTokenSource.FACEBOOK_APPLICATION_WEB     // Catch:{ FacebookException -> 0x004c }
            java.lang.String r10 = r12.getApplicationId()     // Catch:{ FacebookException -> 0x004c }
            com.facebook.AccessToken r6 = createAccessTokenFromWebBundle(r8, r5, r9, r10)     // Catch:{ FacebookException -> 0x004c }
            com.facebook.login.LoginClient$Result r7 = com.facebook.login.LoginClient.Result.createTokenResult(r12, r6)     // Catch:{ FacebookException -> 0x004c }
        L_0x0049:
            return r7
        L_0x004a:
            r2 = r7
            goto L_0x001d
        L_0x004c:
            r4 = move-exception
            java.lang.String r8 = r4.getMessage()
            com.facebook.login.LoginClient$Result r7 = com.facebook.login.LoginClient.Result.createErrorResult(r12, r7, r8)
            goto L_0x0049
        L_0x0056:
            java.util.Collection<java.lang.String> r8 = com.facebook.internal.ServerProtocol.errorsProxyAuthDisabled
            boolean r8 = r8.contains(r1)
            if (r8 != 0) goto L_0x0049
            java.util.Collection<java.lang.String> r8 = com.facebook.internal.ServerProtocol.errorsUserCanceled
            boolean r8 = r8.contains(r1)
            if (r8 == 0) goto L_0x006b
            com.facebook.login.LoginClient$Result r7 = com.facebook.login.LoginClient.Result.createCancelResult(r12, r7)
            goto L_0x0049
        L_0x006b:
            com.facebook.login.LoginClient$Result r7 = com.facebook.login.LoginClient.Result.createErrorResult(r12, r1, r3, r2)
            goto L_0x0049
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.login.NativeAppLoginMethodHandler.handleResultOk(com.facebook.login.LoginClient$Request, android.content.Intent):com.facebook.login.LoginClient$Result");
    }

    private Result handleResultCancel(Request request, Intent data) {
        Bundle extras = data.getExtras();
        String error = getError(extras);
        String errorCode = extras.get(NativeProtocol.BRIDGE_ARG_ERROR_CODE) != null ? extras.get(NativeProtocol.BRIDGE_ARG_ERROR_CODE).toString() : null;
        if (ServerProtocol.errorConnectionFailure.equals(errorCode)) {
            return Result.createErrorResult(request, error, getErrorMessage(extras), errorCode);
        }
        return Result.createCancelResult(request, error);
    }

    private String getError(Bundle extras) {
        String error = extras.getString("error");
        if (error == null) {
            return extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_TYPE);
        }
        return error;
    }

    private String getErrorMessage(Bundle extras) {
        String errorMessage = extras.getString(AnalyticsEvents.PARAMETER_SHARE_ERROR_MESSAGE);
        if (errorMessage == null) {
            return extras.getString(NativeProtocol.BRIDGE_ARG_ERROR_DESCRIPTION);
        }
        return errorMessage;
    }

    /* access modifiers changed from: protected */
    public boolean tryIntent(Intent intent, int requestCode) {
        if (intent == null) {
            return false;
        }
        try {
            this.loginClient.getFragment().startActivityForResult(intent, requestCode);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}
