package org.apache.cordova.camera;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.p000v4.content.FileProvider;
import android.util.Base64;
import com.facebook.internal.ServerProtocol;
import com.facebook.share.internal.ShareConstants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.cordova.BuildHelper;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

public class CameraLauncher extends CordovaPlugin implements MediaScannerConnectionClient {
    private static final int ALLMEDIA = 2;
    private static final int CAMERA = 1;
    private static final int CROP_CAMERA = 100;
    private static final int DATA_URL = 0;
    private static final int FILE_URI = 1;
    private static final String GET_All = "Get All";
    private static final String GET_PICTURE = "Get Picture";
    private static final String GET_VIDEO = "Get Video";
    private static final int JPEG = 0;
    private static final String LOG_TAG = "CameraLauncher";
    private static final int NATIVE_URI = 2;
    public static final int PERMISSION_DENIED_ERROR = 20;
    private static final int PHOTOLIBRARY = 0;
    private static final int PICTURE = 0;
    private static final int PNG = 1;
    private static final int SAVEDPHOTOALBUM = 2;
    public static final int SAVE_TO_ALBUM_SEC = 1;
    public static final int TAKE_PIC_SEC = 0;
    private static final int VIDEO = 1;
    protected static final String[] permissions = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private boolean allowEdit;
    private String applicationId;
    public CallbackContext callbackContext;
    private MediaScannerConnection conn;
    private boolean correctOrientation;
    private Uri croppedUri;
    private int destType;
    private int encodingType;
    private ExifHelper exifData;
    private CordovaUri imageUri;
    private int mQuality;
    private int mediaType;
    private int numPics;
    private boolean orientationCorrected;
    private boolean saveToPhotoAlbum;
    private Uri scanMe;
    private int srcType;
    private int targetHeight;
    private int targetWidth;

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext2) throws JSONException {
        this.callbackContext = callbackContext2;
        this.applicationId = (String) BuildHelper.getBuildConfigValue(this.cordova.getActivity(), "APPLICATION_ID");
        this.applicationId = this.preferences.getString("applicationId", this.applicationId);
        if (!action.equals("takePicture")) {
            return false;
        }
        this.srcType = 1;
        this.destType = 1;
        this.saveToPhotoAlbum = false;
        this.targetHeight = 0;
        this.targetWidth = 0;
        this.encodingType = 0;
        this.mediaType = 0;
        this.mQuality = 50;
        this.destType = args.getInt(1);
        this.srcType = args.getInt(2);
        this.mQuality = args.getInt(0);
        this.targetWidth = args.getInt(3);
        this.targetHeight = args.getInt(4);
        this.encodingType = args.getInt(5);
        this.mediaType = args.getInt(6);
        this.allowEdit = args.getBoolean(7);
        this.correctOrientation = args.getBoolean(8);
        this.saveToPhotoAlbum = args.getBoolean(9);
        if (this.targetWidth < 1) {
            this.targetWidth = -1;
        }
        if (this.targetHeight < 1) {
            this.targetHeight = -1;
        }
        if (this.targetHeight == -1 && this.targetWidth == -1 && this.mQuality == 100 && !this.correctOrientation && this.encodingType == 1 && this.srcType == 1) {
            this.encodingType = 0;
        }
        try {
            if (this.srcType == 1) {
                callTakePicture(this.destType, this.encodingType);
            } else if (this.srcType == 0 || this.srcType == 2) {
                if (!PermissionHelper.hasPermission(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                    PermissionHelper.requestPermission(this, 1, "android.permission.READ_EXTERNAL_STORAGE");
                } else {
                    getImage(this.srcType, this.destType, this.encodingType);
                }
            }
            PluginResult r = new PluginResult(Status.NO_RESULT);
            r.setKeepCallback(true);
            callbackContext2.sendPluginResult(r);
            return true;
        } catch (IllegalArgumentException e) {
            callbackContext2.error("Illegal Argument Exception");
            callbackContext2.sendPluginResult(new PluginResult(Status.ERROR));
            return true;
        }
    }

    private String getTempDirectoryPath() {
        File cache;
        if (Environment.getExternalStorageState().equals("mounted")) {
            cache = this.cordova.getActivity().getExternalCacheDir();
        } else {
            cache = this.cordova.getActivity().getCacheDir();
        }
        cache.mkdirs();
        return cache.getAbsolutePath();
    }

    public void callTakePicture(int returnType, int encodingType2) {
        boolean saveAlbumPermission;
        if (!PermissionHelper.hasPermission(this, "android.permission.READ_EXTERNAL_STORAGE") || !PermissionHelper.hasPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            saveAlbumPermission = false;
        } else {
            saveAlbumPermission = true;
        }
        boolean takePicturePermission = PermissionHelper.hasPermission(this, "android.permission.CAMERA");
        if (!takePicturePermission) {
            takePicturePermission = true;
            try {
                String[] permissionsInPackage = this.cordova.getActivity().getPackageManager().getPackageInfo(this.cordova.getActivity().getPackageName(), 4096).requestedPermissions;
                if (permissionsInPackage != null) {
                    int length = permissionsInPackage.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (permissionsInPackage[i].equals("android.permission.CAMERA")) {
                            takePicturePermission = false;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
            } catch (NameNotFoundException e) {
            }
        }
        if (takePicturePermission && saveAlbumPermission) {
            takePicture(returnType, encodingType2);
        } else if (saveAlbumPermission && !takePicturePermission) {
            PermissionHelper.requestPermission(this, 0, "android.permission.CAMERA");
        } else if (saveAlbumPermission || !takePicturePermission) {
            PermissionHelper.requestPermissions(this, 0, permissions);
        } else {
            PermissionHelper.requestPermissions(this, 0, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"});
        }
    }

    public void takePicture(int returnType, int encodingType2) {
        this.numPics = queryImgDB(whichContentStore()).getCount();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        this.imageUri = new CordovaUri(FileProvider.getUriForFile(this.cordova.getActivity(), this.applicationId + ".provider", createCaptureFile(encodingType2)));
        intent.putExtra("output", this.imageUri.getCorrectUri());
        intent.addFlags(2);
        if (this.cordova == null) {
            return;
        }
        if (intent.resolveActivity(this.cordova.getActivity().getPackageManager()) != null) {
            this.cordova.startActivityForResult(this, intent, returnType + 32 + 1);
        } else {
            LOG.m7d(LOG_TAG, "Error: You don't have a default camera.  Your device may not be CTS complaint.");
        }
    }

    private File createCaptureFile(int encodingType2) {
        return createCaptureFile(encodingType2, "");
    }

    private File createCaptureFile(int encodingType2, String fileName) {
        String fileName2;
        if (fileName.isEmpty()) {
            fileName = ".Pic";
        }
        if (encodingType2 == 0) {
            fileName2 = fileName + ".jpg";
        } else if (encodingType2 == 1) {
            fileName2 = fileName + ".png";
        } else {
            throw new IllegalArgumentException("Invalid Encoding Type: " + encodingType2);
        }
        return new File(getTempDirectoryPath(), fileName2);
    }

    public void getImage(int srcType2, int returnType, int encodingType2) {
        Intent intent = new Intent();
        String title = GET_PICTURE;
        this.croppedUri = null;
        if (this.mediaType == 0) {
            intent.setType("image/*");
            if (this.allowEdit) {
                intent.setAction("android.intent.action.PICK");
                intent.putExtra("crop", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
                if (this.targetWidth > 0) {
                    intent.putExtra("outputX", this.targetWidth);
                }
                if (this.targetHeight > 0) {
                    intent.putExtra("outputY", this.targetHeight);
                }
                if (this.targetHeight > 0 && this.targetWidth > 0 && this.targetWidth == this.targetHeight) {
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                }
                this.croppedUri = Uri.fromFile(createCaptureFile(0));
                intent.putExtra("output", this.croppedUri);
            } else {
                intent.setAction("android.intent.action.GET_CONTENT");
                intent.addCategory("android.intent.category.OPENABLE");
            }
        } else if (this.mediaType == 1) {
            intent.setType("video/*");
            title = GET_VIDEO;
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.addCategory("android.intent.category.OPENABLE");
        } else if (this.mediaType == 2) {
            intent.setType("*/*");
            title = GET_All;
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.addCategory("android.intent.category.OPENABLE");
        }
        if (this.cordova != null) {
            this.cordova.startActivityForResult(this, Intent.createChooser(intent, new String(title)), ((srcType2 + 1) * 16) + returnType + 1);
        }
    }

    private void performCrop(Uri picUri, int destType2, Intent cameraIntent) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            if (this.targetWidth > 0) {
                cropIntent.putExtra("outputX", this.targetWidth);
            }
            if (this.targetHeight > 0) {
                cropIntent.putExtra("outputY", this.targetHeight);
            }
            if (this.targetHeight > 0 && this.targetWidth > 0 && this.targetWidth == this.targetHeight) {
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
            }
            this.croppedUri = Uri.fromFile(createCaptureFile(this.encodingType, System.currentTimeMillis() + ""));
            cropIntent.addFlags(1);
            cropIntent.addFlags(2);
            cropIntent.putExtra("output", this.croppedUri);
            if (this.cordova != null) {
                this.cordova.startActivityForResult(this, cropIntent, destType2 + 100);
            }
        } catch (ActivityNotFoundException e) {
            LOG.m10e(LOG_TAG, "Crop operation not supported on this device");
            try {
                processResultFromCamera(destType2, cameraIntent);
            } catch (IOException e2) {
                e2.printStackTrace();
                LOG.m10e(LOG_TAG, "Unable to write to file");
            }
        }
    }

    private void processResultFromCamera(int destType2, Intent intent) throws IOException {
        String sourcePath;
        int rotate = 0;
        ExifHelper exif = new ExifHelper();
        if (!this.allowEdit || this.croppedUri == null) {
            sourcePath = this.imageUri.getFilePath();
        } else {
            sourcePath = FileHelper.stripFileProtocol(this.croppedUri.toString());
        }
        if (this.encodingType == 0) {
            try {
                exif.createInFile(sourcePath);
                exif.readExifData();
                rotate = exif.getOrientation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap bitmap = null;
        Uri galleryUri = null;
        if (this.saveToPhotoAlbum) {
            galleryUri = Uri.fromFile(new File(getPicturesPath()));
            if (!this.allowEdit || this.croppedUri == null) {
                writeUncompressedImage(this.imageUri.getFileUri(), galleryUri);
            } else {
                writeUncompressedImage(this.croppedUri, galleryUri);
            }
            refreshGallery(galleryUri);
        }
        if (destType2 == 0) {
            bitmap = getScaledAndRotatedBitmap(sourcePath);
            if (bitmap == null) {
                bitmap = (Bitmap) intent.getExtras().get(ShareConstants.WEB_DIALOG_PARAM_DATA);
            }
            if (bitmap == null) {
                LOG.m7d(LOG_TAG, "I either have a null image path or bitmap");
                failPicture("Unable to create bitmap!");
                return;
            }
            processPicture(bitmap, this.encodingType);
            if (!this.saveToPhotoAlbum) {
                checkForDuplicateImage(0);
            }
        } else if (destType2 != 1 && destType2 != 2) {
            throw new IllegalStateException();
        } else if (this.targetHeight != -1 || this.targetWidth != -1 || this.mQuality != 100 || this.correctOrientation) {
            Uri uri = Uri.fromFile(createCaptureFile(this.encodingType, System.currentTimeMillis() + ""));
            bitmap = getScaledAndRotatedBitmap(sourcePath);
            if (bitmap == null) {
                LOG.m7d(LOG_TAG, "I either have a null image path or bitmap");
                failPicture("Unable to create bitmap!");
                return;
            }
            OutputStream os = this.cordova.getActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(this.encodingType == 0 ? CompressFormat.JPEG : CompressFormat.PNG, this.mQuality, os);
            os.close();
            if (this.encodingType == 0) {
                String exifPath = uri.getPath();
                if (rotate != 1) {
                    exif.resetOrientation();
                }
                exif.createOutFile(exifPath);
                exif.writeExifData();
            }
            this.callbackContext.success(uri.toString());
        } else if (this.saveToPhotoAlbum) {
            this.callbackContext.success(galleryUri.toString());
        } else {
            Uri uri2 = Uri.fromFile(createCaptureFile(this.encodingType, System.currentTimeMillis() + ""));
            if (!this.allowEdit || this.croppedUri == null) {
                writeUncompressedImage(this.imageUri.getFileUri(), uri2);
            } else {
                writeUncompressedImage(Uri.fromFile(new File(getFileNameFromUri(this.croppedUri))), uri2);
            }
            this.callbackContext.success(uri2.toString());
        }
        cleanup(1, this.imageUri.getFileUri(), galleryUri, bitmap);
    }

    private String getPicturesPath() {
        String imageFileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + (this.encodingType == 0 ? ".jpg" : ".png");
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        return storageDir.getAbsolutePath() + "/" + imageFileName;
    }

    private void refreshGallery(Uri contentUri) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(contentUri);
        this.cordova.getActivity().sendBroadcast(mediaScanIntent);
    }

    private String getMimetypeForFormat(int outputFormat) {
        if (outputFormat == 1) {
            return "image/png";
        }
        if (outputFormat == 0) {
            return "image/jpeg";
        }
        return "";
    }

    private String outputModifiedBitmap(Bitmap bitmap, Uri uri) throws IOException {
        String fileName;
        String realPath = FileHelper.getRealPath(uri, this.cordova);
        if (realPath != null) {
            fileName = realPath.substring(realPath.lastIndexOf(47) + 1);
        } else {
            fileName = "modified." + (this.encodingType == 0 ? "jpg" : "png");
        }
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String modifiedPath = getTempDirectoryPath() + "/" + fileName;
        OutputStream os = new FileOutputStream(modifiedPath);
        bitmap.compress(this.encodingType == 0 ? CompressFormat.JPEG : CompressFormat.PNG, this.mQuality, os);
        os.close();
        if (this.exifData != null && this.encodingType == 0) {
            try {
                if (this.correctOrientation && this.orientationCorrected) {
                    this.exifData.resetOrientation();
                }
                this.exifData.createOutFile(modifiedPath);
                this.exifData.writeExifData();
                this.exifData = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modifiedPath;
    }

    /* access modifiers changed from: private */
    public void processResultFromGallery(int destType2, Intent intent) {
        Uri uri = intent.getData();
        if (uri == null) {
            if (this.croppedUri != null) {
                uri = this.croppedUri;
            } else {
                failPicture("null data from photo library");
                return;
            }
        }
        String fileLocation = FileHelper.getRealPath(uri, this.cordova);
        LOG.m7d(LOG_TAG, "File locaton is: " + fileLocation);
        if (this.mediaType != 0) {
            this.callbackContext.success(fileLocation);
            return;
        }
        String uriString = uri.toString();
        String mimeType = FileHelper.getMimeType(uriString, this.cordova);
        if (this.targetHeight == -1 && this.targetWidth == -1 && ((destType2 == 1 || destType2 == 2) && !this.correctOrientation && mimeType.equalsIgnoreCase(getMimetypeForFormat(this.encodingType)))) {
            this.callbackContext.success(uriString);
        } else if ("image/jpeg".equalsIgnoreCase(mimeType) || "image/png".equalsIgnoreCase(mimeType)) {
            Bitmap bitmap = null;
            try {
                bitmap = getScaledAndRotatedBitmap(uriString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) {
                LOG.m7d(LOG_TAG, "I either have a null image path or bitmap");
                failPicture("Unable to create bitmap!");
                return;
            }
            if (destType2 == 0) {
                processPicture(bitmap, this.encodingType);
            } else if (destType2 == 1 || destType2 == 2) {
                if ((this.targetHeight <= 0 || this.targetWidth <= 0) && ((!this.correctOrientation || !this.orientationCorrected) && mimeType.equalsIgnoreCase(getMimetypeForFormat(this.encodingType)))) {
                    this.callbackContext.success(fileLocation);
                } else {
                    try {
                        this.callbackContext.success("file://" + outputModifiedBitmap(bitmap, uri) + "?" + System.currentTimeMillis());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        failPicture("Error retrieving image.");
                    }
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            System.gc();
        } else {
            LOG.m7d(LOG_TAG, "I either have a null image path or bitmap");
            failPicture("Unable to retrieve path to picture!");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        int srcType2 = (requestCode / 16) - 1;
        int destType2 = (requestCode % 16) - 1;
        if (requestCode >= 100) {
            if (resultCode == -1) {
                try {
                    processResultFromCamera(requestCode - 100, intent);
                } catch (IOException e) {
                    e.printStackTrace();
                    LOG.m10e(LOG_TAG, "Unable to write to file");
                }
            } else if (resultCode == 0) {
                failPicture("No Image Selected");
            } else {
                failPicture("Did not complete!");
            }
        } else if (srcType2 == 1) {
            if (resultCode == -1) {
                try {
                    if (this.allowEdit) {
                        performCrop(FileProvider.getUriForFile(this.cordova.getActivity(), this.applicationId + ".provider", createCaptureFile(this.encodingType)), destType2, intent);
                    } else {
                        processResultFromCamera(destType2, intent);
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    failPicture("Error capturing image.");
                }
            } else if (resultCode == 0) {
                failPicture("No Image Selected");
            } else {
                failPicture("Did not complete!");
            }
        } else if (srcType2 != 0 && srcType2 != 2) {
        } else {
            if (resultCode == -1 && intent != null) {
                final Intent i = intent;
                final int finalDestType = destType2;
                this.cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        CameraLauncher.this.processResultFromGallery(finalDestType, i);
                    }
                });
            } else if (resultCode == 0) {
                failPicture("No Image Selected");
            } else {
                failPicture("Selection did not complete!");
            }
        }
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == 6) {
            return 90;
        }
        if (exifOrientation == 3) {
            return 180;
        }
        if (exifOrientation == 8) {
            return 270;
        }
        return 0;
    }

    private void writeUncompressedImage(InputStream fis, Uri dest) throws FileNotFoundException, IOException {
        OutputStream os = null;
        try {
            os = this.cordova.getActivity().getContentResolver().openOutputStream(dest);
            byte[] buffer = new byte[4096];
            while (true) {
                int len = fis.read(buffer);
                if (len == -1) {
                    break;
                }
                os.write(buffer, 0, len);
            }
            os.flush();
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    LOG.m7d(LOG_TAG, "Exception while closing output stream.");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e2) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                }
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e3) {
                    LOG.m7d(LOG_TAG, "Exception while closing output stream.");
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                }
            }
        }
    }

    private void writeUncompressedImage(Uri src, Uri dest) throws FileNotFoundException, IOException {
        writeUncompressedImage((InputStream) new FileInputStream(FileHelper.stripFileProtocol(src.toString())), dest);
    }

    private Uri getUriFromMediaStore() {
        ContentValues values = new ContentValues();
        values.put("mime_type", "image/jpeg");
        try {
            return this.cordova.getActivity().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
        } catch (RuntimeException e) {
            LOG.m7d(LOG_TAG, "Can't write to external media storage.");
            try {
                return this.cordova.getActivity().getContentResolver().insert(Media.INTERNAL_CONTENT_URI, values);
            } catch (RuntimeException e2) {
                LOG.m7d(LOG_TAG, "Can't write to internal media storage.");
                return null;
            }
        }
    }

    private Bitmap getScaledAndRotatedBitmap(String imageUrl) throws IOException {
        Options options;
        InputStream fileStream;
        int rotatedWidth;
        int rotatedHeight;
        int scaledHeight;
        if (this.targetWidth > 0 || this.targetHeight > 0 || this.correctOrientation) {
            File localFile = null;
            Uri galleryUri = null;
            int rotate = 0;
            try {
                InputStream fileStream2 = FileHelper.getInputStreamFromUriString(imageUrl, this.cordova);
                if (fileStream2 != null) {
                    File file = new File(getTempDirectoryPath() + ("IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + (this.encodingType == 0 ? ".jpg" : ".png")));
                    try {
                        galleryUri = Uri.fromFile(file);
                        writeUncompressedImage(fileStream2, galleryUri);
                        try {
                            if ("image/jpeg".equalsIgnoreCase(FileHelper.getMimeType(imageUrl.toString(), this.cordova))) {
                                String filePath = galleryUri.toString().replace("file://", "");
                                this.exifData = new ExifHelper();
                                this.exifData.createInFile(filePath);
                                if (this.correctOrientation) {
                                    rotate = exifToDegrees(new ExifInterface(filePath).getAttributeInt("Orientation", 0));
                                }
                            }
                            localFile = file;
                        } catch (Exception oe) {
                            LOG.m19w(LOG_TAG, "Unable to read Exif data: " + oe.toString());
                            rotate = 0;
                            localFile = file;
                        }
                    } catch (Exception e) {
                        e = e;
                        File file2 = file;
                        LOG.m10e(LOG_TAG, "Exception while getting input stream: " + e.toString());
                        return null;
                    }
                }
                try {
                    options = new Options();
                    options.inJustDecodeBounds = true;
                    fileStream = null;
                    fileStream = FileHelper.getInputStreamFromUriString(galleryUri.toString(), this.cordova);
                    BitmapFactory.decodeStream(fileStream, null, options);
                    if (fileStream != null) {
                        fileStream.close();
                    }
                } catch (IOException e2) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                } catch (Throwable th) {
                    if (localFile != null) {
                        localFile.delete();
                    }
                    throw th;
                }
                if (options.outWidth != 0 && options.outHeight != 0) {
                    if (this.targetWidth <= 0 && this.targetHeight <= 0) {
                        this.targetWidth = options.outWidth;
                        this.targetHeight = options.outHeight;
                    }
                    boolean rotated = false;
                    if (rotate == 90 || rotate == 270) {
                        rotatedWidth = options.outHeight;
                        rotatedHeight = options.outWidth;
                        rotated = true;
                    } else {
                        rotatedWidth = options.outWidth;
                        rotatedHeight = options.outHeight;
                    }
                    int[] widthHeight = calculateAspectRatio(rotatedWidth, rotatedHeight);
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = calculateSampleSize(rotatedWidth, rotatedHeight, widthHeight[0], widthHeight[1]);
                    fileStream = FileHelper.getInputStreamFromUriString(galleryUri.toString(), this.cordova);
                    Bitmap unscaledBitmap = BitmapFactory.decodeStream(fileStream, null, options);
                    if (fileStream != null) {
                        try {
                            fileStream.close();
                        } catch (IOException e3) {
                            LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                        }
                    }
                    if (unscaledBitmap != null) {
                        int scaledWidth = !rotated ? widthHeight[0] : widthHeight[1];
                        if (!rotated) {
                            scaledHeight = widthHeight[1];
                        } else {
                            scaledHeight = widthHeight[0];
                        }
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(unscaledBitmap, scaledWidth, scaledHeight, true);
                        if (scaledBitmap != unscaledBitmap) {
                            unscaledBitmap.recycle();
                        }
                        if (this.correctOrientation && rotate != 0) {
                            Matrix matrix = new Matrix();
                            matrix.setRotate((float) rotate);
                            try {
                                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                                this.orientationCorrected = true;
                            } catch (OutOfMemoryError e4) {
                                this.orientationCorrected = false;
                            }
                        }
                        if (localFile != null) {
                            localFile.delete();
                        }
                        return scaledBitmap;
                    } else if (localFile == null) {
                        return null;
                    } else {
                        localFile.delete();
                        return null;
                    }
                } else if (localFile == null) {
                    return null;
                } else {
                    localFile.delete();
                    return null;
                }
            } catch (Exception e5) {
                e = e5;
                LOG.m10e(LOG_TAG, "Exception while getting input stream: " + e.toString());
                return null;
            }
        } else {
            InputStream fileStream3 = null;
            Bitmap image = null;
            try {
                fileStream3 = FileHelper.getInputStreamFromUriString(imageUrl, this.cordova);
                Bitmap image2 = BitmapFactory.decodeStream(fileStream3);
                if (fileStream3 == null) {
                    return image2;
                }
                try {
                    fileStream3.close();
                    return image2;
                } catch (IOException e6) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                    return image2;
                }
            } catch (OutOfMemoryError e7) {
                this.callbackContext.error(e7.getLocalizedMessage());
                if (fileStream3 == null) {
                    return image;
                }
                try {
                    fileStream3.close();
                    return image;
                } catch (IOException e8) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                    return image;
                }
            } catch (Exception e9) {
                this.callbackContext.error(e9.getLocalizedMessage());
                if (fileStream3 == null) {
                    return image;
                }
                try {
                    fileStream3.close();
                    return image;
                } catch (IOException e10) {
                    LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                    return image;
                }
            } finally {
                if (fileStream3 != null) {
                    try {
                        fileStream3.close();
                    } catch (IOException e11) {
                        LOG.m7d(LOG_TAG, "Exception while closing file input stream.");
                    }
                }
            }
        }
    }

    public int[] calculateAspectRatio(int origWidth, int origHeight) {
        int newWidth = this.targetWidth;
        int newHeight = this.targetHeight;
        if (newWidth <= 0 && newHeight <= 0) {
            newWidth = origWidth;
            newHeight = origHeight;
        } else if (newWidth > 0 && newHeight <= 0) {
            newHeight = (int) ((((double) newWidth) / ((double) origWidth)) * ((double) origHeight));
        } else if (newWidth > 0 || newHeight <= 0) {
            double newRatio = ((double) newWidth) / ((double) newHeight);
            double origRatio = ((double) origWidth) / ((double) origHeight);
            if (origRatio > newRatio) {
                newHeight = (newWidth * origHeight) / origWidth;
            } else if (origRatio < newRatio) {
                newWidth = (newHeight * origWidth) / origHeight;
            }
        } else {
            newWidth = (int) ((((double) newHeight) / ((double) origHeight)) * ((double) origWidth));
        }
        return new int[]{newWidth, newHeight};
    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        if (((float) srcWidth) / ((float) srcHeight) > ((float) dstWidth) / ((float) dstHeight)) {
            return srcWidth / dstWidth;
        }
        return srcHeight / dstHeight;
    }

    private Cursor queryImgDB(Uri contentStore) {
        return this.cordova.getActivity().getContentResolver().query(contentStore, new String[]{"_id"}, null, null, null);
    }

    private void cleanup(int imageType, Uri oldImage, Uri newImage, Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
        new File(FileHelper.stripFileProtocol(oldImage.toString())).delete();
        checkForDuplicateImage(imageType);
        if (this.saveToPhotoAlbum && newImage != null) {
            scanForGallery(newImage);
        }
        System.gc();
    }

    private void checkForDuplicateImage(int type) {
        int diff = 1;
        Uri contentStore = whichContentStore();
        Cursor cursor = queryImgDB(contentStore);
        int currentNumOfImages = cursor.getCount();
        if (type == 1 && this.saveToPhotoAlbum) {
            diff = 2;
        }
        if (currentNumOfImages - this.numPics == diff) {
            cursor.moveToLast();
            int id = Integer.valueOf(cursor.getString(cursor.getColumnIndex("_id"))).intValue();
            if (diff == 2) {
                id--;
            }
            this.cordova.getActivity().getContentResolver().delete(Uri.parse(contentStore + "/" + id), null, null);
            cursor.close();
        }
    }

    private Uri whichContentStore() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Media.EXTERNAL_CONTENT_URI;
        }
        return Media.INTERNAL_CONTENT_URI;
    }

    public void processPicture(Bitmap bitmap, int encodingType2) {
        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        try {
            if (bitmap.compress(encodingType2 == 0 ? CompressFormat.JPEG : CompressFormat.PNG, this.mQuality, jpeg_data)) {
                this.callbackContext.success(new String(Base64.encode(jpeg_data.toByteArray(), 2)));
            }
        } catch (Exception e) {
            failPicture("Error compressing image.");
        }
    }

    public void failPicture(String err) {
        this.callbackContext.error(err);
    }

    private void scanForGallery(Uri newImage) {
        this.scanMe = newImage;
        if (this.conn != null) {
            this.conn.disconnect();
        }
        this.conn = new MediaScannerConnection(this.cordova.getActivity().getApplicationContext(), this);
        this.conn.connect();
    }

    public void onMediaScannerConnected() {
        try {
            this.conn.scanFile(this.scanMe.toString(), "image/*");
        } catch (IllegalStateException e) {
            LOG.m10e(LOG_TAG, "Can't scan file in MediaScanner after taking picture");
        }
    }

    public void onScanCompleted(String path, Uri uri) {
        this.conn.disconnect();
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions2, int[] grantResults) throws JSONException {
        for (int r : grantResults) {
            if (r == -1) {
                this.callbackContext.sendPluginResult(new PluginResult(Status.ERROR, 20));
                return;
            }
        }
        switch (requestCode) {
            case 0:
                takePicture(this.destType, this.encodingType);
                return;
            case 1:
                getImage(this.srcType, this.destType, this.encodingType);
                return;
            default:
                return;
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle state = new Bundle();
        state.putInt("destType", this.destType);
        state.putInt("srcType", this.srcType);
        state.putInt("mQuality", this.mQuality);
        state.putInt("targetWidth", this.targetWidth);
        state.putInt("targetHeight", this.targetHeight);
        state.putInt("encodingType", this.encodingType);
        state.putInt("mediaType", this.mediaType);
        state.putInt("numPics", this.numPics);
        state.putBoolean("allowEdit", this.allowEdit);
        state.putBoolean("correctOrientation", this.correctOrientation);
        state.putBoolean("saveToPhotoAlbum", this.saveToPhotoAlbum);
        if (this.croppedUri != null) {
            state.putString("croppedUri", this.croppedUri.toString());
        }
        if (this.imageUri != null) {
            state.putString("imageUri", this.imageUri.getFileUri().toString());
        }
        return state;
    }

    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext2) {
        this.destType = state.getInt("destType");
        this.srcType = state.getInt("srcType");
        this.mQuality = state.getInt("mQuality");
        this.targetWidth = state.getInt("targetWidth");
        this.targetHeight = state.getInt("targetHeight");
        this.encodingType = state.getInt("encodingType");
        this.mediaType = state.getInt("mediaType");
        this.numPics = state.getInt("numPics");
        this.allowEdit = state.getBoolean("allowEdit");
        this.correctOrientation = state.getBoolean("correctOrientation");
        this.saveToPhotoAlbum = state.getBoolean("saveToPhotoAlbum");
        if (state.containsKey("croppedUri")) {
            this.croppedUri = Uri.parse(state.getString("croppedUri"));
        }
        if (state.containsKey("imageUri")) {
            this.imageUri = new CordovaUri(Uri.parse(state.getString("imageUri")));
        }
        this.callbackContext = callbackContext2;
    }

    private String getFileNameFromUri(Uri uri) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + uri.toString().split("external_files")[1];
    }
}
