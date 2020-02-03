package org.apache.cordova.camera;

import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;

public class CordovaUri {
    private Uri androidUri;
    private String fileName;
    private Uri fileUri;

    CordovaUri(Uri inputUri) {
        if (inputUri.getScheme().equals("content")) {
            this.androidUri = inputUri;
            this.fileName = getFileNameFromUri(this.androidUri);
            this.fileUri = Uri.parse("file://" + this.fileName);
            return;
        }
        this.fileUri = inputUri;
        this.fileName = FileHelper.stripFileProtocol(inputUri.toString());
    }

    public Uri getFileUri() {
        return this.fileUri;
    }

    public String getFilePath() {
        return this.fileName;
    }

    public Uri getCorrectUri() {
        if (VERSION.SDK_INT >= 23) {
            return this.androidUri;
        }
        return this.fileUri;
    }

    private String getFileNameFromUri(Uri uri) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + uri.toString().split("external_files")[1];
    }
}
