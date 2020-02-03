package com.facebook.share.internal;

import android.support.annotation.Nullable;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenGraphJSONUtility {

    public interface PhotoJSONProcessor {
        JSONObject toJSONObject(SharePhoto sharePhoto);
    }

    public static JSONObject toJSONObject(ShareOpenGraphAction action, PhotoJSONProcessor photoJSONProcessor) throws JSONException {
        JSONObject result = new JSONObject();
        for (String key : action.keySet()) {
            result.put(key, toJSONValue(action.get(key), photoJSONProcessor));
        }
        return result;
    }

    private static JSONObject toJSONObject(ShareOpenGraphObject object, PhotoJSONProcessor photoJSONProcessor) throws JSONException {
        JSONObject result = new JSONObject();
        for (String key : object.keySet()) {
            result.put(key, toJSONValue(object.get(key), photoJSONProcessor));
        }
        return result;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<java.lang.Object>, for r4v0, types: [java.util.List, java.util.List<java.lang.Object>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.json.JSONArray toJSONArray(java.util.List<java.lang.Object> r4, com.facebook.share.internal.OpenGraphJSONUtility.PhotoJSONProcessor r5) throws org.json.JSONException {
        /*
            org.json.JSONArray r1 = new org.json.JSONArray
            r1.<init>()
            java.util.Iterator r2 = r4.iterator()
        L_0x0009:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x001b
            java.lang.Object r0 = r2.next()
            java.lang.Object r3 = toJSONValue(r0, r5)
            r1.put(r3)
            goto L_0x0009
        L_0x001b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.share.internal.OpenGraphJSONUtility.toJSONArray(java.util.List, com.facebook.share.internal.OpenGraphJSONUtility$PhotoJSONProcessor):org.json.JSONArray");
    }

    public static Object toJSONValue(@Nullable Object object, PhotoJSONProcessor photoJSONProcessor) throws JSONException {
        if (object == null) {
            return JSONObject.NULL;
        }
        if ((object instanceof String) || (object instanceof Boolean) || (object instanceof Double) || (object instanceof Float) || (object instanceof Integer) || (object instanceof Long)) {
            return object;
        }
        if (object instanceof SharePhoto) {
            if (photoJSONProcessor != null) {
                return photoJSONProcessor.toJSONObject((SharePhoto) object);
            }
            return null;
        } else if (object instanceof ShareOpenGraphObject) {
            return toJSONObject((ShareOpenGraphObject) object, photoJSONProcessor);
        } else {
            if (object instanceof List) {
                return toJSONArray((List) object, photoJSONProcessor);
            }
            throw new IllegalArgumentException("Invalid object found for JSON serialization: " + object.toString());
        }
    }

    private OpenGraphJSONUtility() {
    }
}
