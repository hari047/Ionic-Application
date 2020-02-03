package com.facebook.marketing.internal;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.share.internal.MessengerShareContentUtility;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class MarketingUtils {
    private static final String TAG = MarketingUtils.class.getCanonicalName();

    public static String getAppVersion() {
        Context context = FacebookSdk.getApplicationContext();
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Failed to get app version.", e);
            return "";
        }
    }

    public static boolean isEmulator() {
        if (Build.FINGERPRINT.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE) || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || Build.MANUFACTURER.contains("Genymotion") || ((Build.BRAND.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE) && Build.DEVICE.startsWith(MessengerShareContentUtility.TEMPLATE_GENERIC_TYPE)) || "google_sdk".equals(Build.PRODUCT))) {
            return true;
        }
        return false;
    }

    public static double normalizePrice(String value) {
        try {
            return NumberFormat.getNumberInstance(Locale.getDefault()).parse(value.replaceAll("[^\\d,.+-]", "")).doubleValue();
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing price: ", e);
            return 0.0d;
        }
    }
}
