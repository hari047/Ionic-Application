package com.facebook.appevents.internal;

import android.os.Looper;
import com.facebook.internal.Utility;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppEventUtility {
    private static final String regex = "[-+]*\\d+([\\,\\.]\\d+)*([\\.\\,]\\d+)?";

    public static void assertIsNotMainThread() {
    }

    public static void assertIsMainThread() {
    }

    public static double normalizePrice(String value) {
        try {
            Matcher matcher = Pattern.compile(regex, 8).matcher(value);
            if (!matcher.find()) {
                return 0.0d;
            }
            return NumberFormat.getNumberInstance(Utility.getCurrentLocale()).parse(matcher.group(0)).doubleValue();
        } catch (ParseException e) {
            return 0.0d;
        }
    }

    private static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
