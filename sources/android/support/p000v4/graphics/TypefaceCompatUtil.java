package android.support.p000v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v4.graphics.TypefaceCompatUtil */
public class TypefaceCompatUtil {
    private static final String CACHE_FILE_PREFIX = ".font";
    private static final String TAG = "TypefaceCompatUtil";

    private TypefaceCompatUtil() {
    }

    @Nullable
    public static File getTempFile(Context context) {
        String prefix = CACHE_FILE_PREFIX + Process.myPid() + "-" + Process.myTid() + "-";
        int i = 0;
        while (i < 100) {
            File file = new File(context.getCacheDir(), prefix + i);
            try {
                if (file.createNewFile()) {
                    return file;
                }
                i++;
            } catch (IOException e) {
            }
        }
        return null;
    }

    @Nullable
    @RequiresApi(19)
    private static ByteBuffer mmap(File file) {
        Throwable th;
        try {
            FileInputStream fis = new FileInputStream(file);
            Throwable th2 = null;
            try {
                FileChannel channel = fis.getChannel();
                MappedByteBuffer map = channel.map(MapMode.READ_ONLY, 0, channel.size());
                if (fis == null) {
                    return map;
                }
                if (0 != 0) {
                    try {
                        fis.close();
                        return map;
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                        return map;
                    }
                } else {
                    fis.close();
                    return map;
                }
            } catch (Throwable th4) {
                Throwable th5 = th4;
                th = r1;
                th = th5;
            }
            if (fis != null) {
                if (th != null) {
                    try {
                        fis.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    fis.close();
                }
            }
            throw th;
            throw th;
        } catch (IOException e) {
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0035, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r10.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0042, code lost:
        if (r2 != null) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        r8.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004f, code lost:
        r1 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0050, code lost:
        r2 = r11;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0069, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x006a, code lost:
        r2.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0072, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:?, code lost:
        r2.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0077, code lost:
        r8.close();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x004f A[ExcHandler: all (th java.lang.Throwable), Splitter:B:4:0x000c] */
    @android.support.annotation.Nullable
    @android.support.annotation.RequiresApi(19)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.nio.ByteBuffer mmap(android.content.Context r13, android.os.CancellationSignal r14, android.net.Uri r15) {
        /*
            android.content.ContentResolver r9 = r13.getContentResolver()
            java.lang.String r1 = "r"
            android.os.ParcelFileDescriptor r8 = r9.openFileDescriptor(r15, r1, r14)     // Catch:{ IOException -> 0x0048 }
            r11 = 0
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            java.io.FileDescriptor r1 = r8.getFileDescriptor()     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            r7.<init>(r1)     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            r10 = 0
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch:{ Throwable -> 0x005b, all -> 0x007b }
            long r4 = r0.size()     // Catch:{ Throwable -> 0x005b, all -> 0x007b }
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ Throwable -> 0x005b, all -> 0x007b }
            r2 = 0
            java.nio.MappedByteBuffer r1 = r0.map(r1, r2, r4)     // Catch:{ Throwable -> 0x005b, all -> 0x007b }
            if (r7 == 0) goto L_0x002d
            if (r10 == 0) goto L_0x004b
            r7.close()     // Catch:{ Throwable -> 0x0035, all -> 0x004f }
        L_0x002d:
            if (r8 == 0) goto L_0x0034
            if (r11 == 0) goto L_0x0057
            r8.close()     // Catch:{ Throwable -> 0x0052 }
        L_0x0034:
            return r1
        L_0x0035:
            r2 = move-exception
            r10.addSuppressed(r2)     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            goto L_0x002d
        L_0x003a:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x003c }
        L_0x003c:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L_0x0040:
            if (r8 == 0) goto L_0x0047
            if (r2 == 0) goto L_0x0077
            r8.close()     // Catch:{ Throwable -> 0x0072 }
        L_0x0047:
            throw r1     // Catch:{ IOException -> 0x0048 }
        L_0x0048:
            r6 = move-exception
            r1 = 0
            goto L_0x0034
        L_0x004b:
            r7.close()     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            goto L_0x002d
        L_0x004f:
            r1 = move-exception
            r2 = r11
            goto L_0x0040
        L_0x0052:
            r2 = move-exception
            r11.addSuppressed(r2)     // Catch:{ IOException -> 0x0048 }
            goto L_0x0034
        L_0x0057:
            r8.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0034
        L_0x005b:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x005d }
        L_0x005d:
            r2 = move-exception
            r12 = r2
            r2 = r1
            r1 = r12
        L_0x0061:
            if (r7 == 0) goto L_0x0068
            if (r2 == 0) goto L_0x006e
            r7.close()     // Catch:{ Throwable -> 0x0069, all -> 0x004f }
        L_0x0068:
            throw r1     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
        L_0x0069:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            goto L_0x0068
        L_0x006e:
            r7.close()     // Catch:{ Throwable -> 0x003a, all -> 0x004f }
            goto L_0x0068
        L_0x0072:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch:{ IOException -> 0x0048 }
            goto L_0x0047
        L_0x0077:
            r8.close()     // Catch:{ IOException -> 0x0048 }
            goto L_0x0047
        L_0x007b:
            r1 = move-exception
            r2 = r10
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.p000v4.graphics.TypefaceCompatUtil.mmap(android.content.Context, android.os.CancellationSignal, android.net.Uri):java.nio.ByteBuffer");
    }

    @Nullable
    @RequiresApi(19)
    public static ByteBuffer copyToDirectBuffer(Context context, Resources res, int id) {
        ByteBuffer byteBuffer = null;
        File tmpFile = getTempFile(context);
        if (tmpFile != null) {
            try {
                if (copyToFile(tmpFile, res, id)) {
                    byteBuffer = mmap(tmpFile);
                    tmpFile.delete();
                }
            } finally {
                tmpFile.delete();
            }
        }
        return byteBuffer;
    }

    public static boolean copyToFile(File file, InputStream is) {
        FileOutputStream os = null;
        try {
            FileOutputStream os2 = new FileOutputStream(file, false);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int readLen = is.read(buffer);
                    if (readLen != -1) {
                        os2.write(buffer, 0, readLen);
                    } else {
                        closeQuietly(os2);
                        FileOutputStream fileOutputStream = os2;
                        return true;
                    }
                }
            } catch (IOException e) {
                e = e;
                os = os2;
                try {
                    Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
                    closeQuietly(os);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(os);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                os = os2;
                closeQuietly(os);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            Log.e(TAG, "Error copying resource contents to temp file: " + e.getMessage());
            closeQuietly(os);
            return false;
        }
    }

    public static boolean copyToFile(File file, Resources res, int id) {
        InputStream is = null;
        try {
            is = res.openRawResource(id);
            return copyToFile(file, is);
        } finally {
            closeQuietly(is);
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
