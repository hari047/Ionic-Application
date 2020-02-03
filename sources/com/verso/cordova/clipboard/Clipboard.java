package com.verso.cordova.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

public class Clipboard extends CordovaPlugin {
    private static final String actionCopy = "copy";
    private static final String actionPaste = "paste";

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        ClipboardManager clipboard = (ClipboardManager) this.cordova.getActivity().getSystemService("clipboard");
        if (action.equals(actionCopy)) {
            try {
                String text = args.getString(0);
                clipboard.setPrimaryClip(ClipData.newPlainText("Text", text));
                callbackContext.success(text);
                return true;
            } catch (JSONException e) {
                callbackContext.sendPluginResult(new PluginResult(Status.JSON_EXCEPTION));
            } catch (Exception e2) {
                callbackContext.sendPluginResult(new PluginResult(Status.ERROR, e2.toString()));
            }
        } else {
            if (action.equals(actionPaste)) {
                if (!clipboard.getPrimaryClipDescription().hasMimeType("text/plain")) {
                    callbackContext.sendPluginResult(new PluginResult(Status.NO_RESULT));
                }
                try {
                    String text2 = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                    if (text2 == null) {
                        text2 = "";
                    }
                    callbackContext.success(text2);
                    return true;
                } catch (Exception e3) {
                    callbackContext.sendPluginResult(new PluginResult(Status.ERROR, e3.toString()));
                }
            }
            return false;
        }
    }
}
