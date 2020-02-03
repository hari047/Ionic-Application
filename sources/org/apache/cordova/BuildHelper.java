package org.apache.cordova;

import android.content.Context;

public class BuildHelper {
    private static String TAG = "BuildHelper";

    public static Object getBuildConfigValue(Context ctx, String key) {
        boolean z = false;
        try {
            return Class.forName(ctx.getPackageName() + ".BuildConfig").getField(key).get(null);
        } catch (ClassNotFoundException e) {
            LOG.m7d(TAG, "Unable to get the BuildConfig, is this built with ANT?");
            e.printStackTrace();
            return z;
        } catch (NoSuchFieldException e2) {
            LOG.m7d(TAG, key + " is not a valid field. Check your build.gradle");
            return z;
        } catch (IllegalAccessException e3) {
            LOG.m7d(TAG, "Illegal Access Exception: Let's print a stack trace.");
            e3.printStackTrace();
            return z;
        }
    }
}
