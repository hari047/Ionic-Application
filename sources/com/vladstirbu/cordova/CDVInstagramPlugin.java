package com.vladstirbu.cordova;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.p000v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import com.facebook.share.widget.ShareDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

@TargetApi(8)
public class CDVInstagramPlugin extends CordovaPlugin {
    private static final FilenameFilter OLD_IMAGE_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.startsWith("instagram");
        }
    };
    CallbackContext cbContext;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.cbContext = callbackContext;
        if (action.equals(ShareDialog.WEB_SHARE_DIALOG)) {
            String imageString = args.getString(0);
            String captionString = args.getString(1);
            new PluginResult(Status.NO_RESULT).setKeepCallback(true);
            share(imageString, captionString);
            return true;
        }
        if (action.equals("isInstalled")) {
            isInstalled();
        } else {
            callbackContext.error("Invalid Action");
        }
        return false;
    }

    private void isInstalled() {
        try {
            this.webView.getContext().getPackageManager().getApplicationInfo("com.instagram.android", 0);
            this.cbContext.success(this.webView.getContext().getPackageManager().getPackageInfo("com.instagram.android", 0).versionName);
        } catch (NameNotFoundException e) {
            this.cbContext.error("Application not installed");
        }
    }

    private void share(String imageString, String captionString) {
        if (imageString == null || imageString.length() <= 0) {
            this.cbContext.error("Expected one non-empty string argument.");
            return;
        }
        byte[] imageData = Base64.decode(imageString, 0);
        File file = null;
        FileOutputStream os = null;
        File parentDir = this.webView.getContext().getExternalFilesDir(null);
        for (File oldImage : parentDir.listFiles(OLD_IMAGE_FILTER)) {
            oldImage.delete();
        }
        try {
            file = File.createTempFile("instagram", ".png", parentDir);
            os = new FileOutputStream(file, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            os.write(imageData);
            os.flush();
            os.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.setType("image/*");
        if (VERSION.SDK_INT < 26) {
            shareIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
        } else {
            new FileProvider();
            shareIntent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.cordova.getActivity().getApplicationContext(), this.cordova.getActivity().getPackageName() + ".provider", file));
            shareIntent.setFlags(1);
        }
        shareIntent.putExtra("android.intent.extra.TEXT", captionString);
        shareIntent.setPackage("com.instagram.android");
        this.cordova.startActivityForResult(this, Intent.createChooser(shareIntent, "Share to"), 12345);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            Log.v("Instagram", "shared ok");
            this.cbContext.success();
        } else if (resultCode == 0) {
            Log.v("Instagram", "share cancelled");
            this.cbContext.error("Share Cancelled");
        }
    }
}
