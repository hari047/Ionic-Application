package p005io.sqlc;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: io.sqlc.SQLiteAndroidDatabase */
class SQLiteAndroidDatabase {
    private static final Pattern DELETE_TABLE_NAME = Pattern.compile("^\\s*DELETE\\s+FROM\\s+(\\S+)", 2);
    private static final Pattern FIRST_WORD = Pattern.compile("^[\\s;]*([^\\s;]+)", 2);
    private static final Pattern UPDATE_TABLE_NAME = Pattern.compile("^\\s*UPDATE\\s+(\\S+)", 2);
    private static final Pattern WHERE_CLAUSE = Pattern.compile("\\s+WHERE\\s+(.+)$", 2);
    private static final boolean isPostHoneycomb = (VERSION.SDK_INT >= 11);
    File dbFile;
    boolean isTransactionActive = false;
    SQLiteDatabase mydb;

    /* renamed from: io.sqlc.SQLiteAndroidDatabase$QueryType */
    enum QueryType {
        update,
        insert,
        delete,
        select,
        begin,
        commit,
        rollback,
        other
    }

    SQLiteAndroidDatabase() {
    }

    /* access modifiers changed from: 0000 */
    public void open(File dbfile) throws Exception {
        this.dbFile = dbfile;
        this.mydb = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
    }

    /* access modifiers changed from: 0000 */
    public void closeDatabaseNow() {
        if (this.mydb != null) {
            if (this.isTransactionActive) {
                this.mydb.endTransaction();
                this.isTransactionActive = false;
            }
            this.mydb.close();
            this.mydb = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public void bugWorkaround() throws Exception {
        closeDatabaseNow();
        open(this.dbFile);
    }

    /* access modifiers changed from: 0000 */
    public void executeSqlBatch(String[] queryarr, JSONArray[] jsonparamsArr, CallbackContext cbc) {
        if (this.mydb == null) {
            cbc.error("INTERNAL PLUGIN ERROR: database not open");
            return;
        }
        int len = queryarr.length;
        JSONArray batchResults = new JSONArray();
        for (int i = 0; i < len; i++) {
            executeSqlBatchStatement(queryarr[i], jsonparamsArr[i], batchResults);
        }
        cbc.success(batchResults);
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x03a2 A[SYNTHETIC, Splitter:B:122:0x03a2] */
    /* JADX WARNING: Removed duplicated region for block: B:132:0x03f2  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a4  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c2  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x011f  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0144  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x0164 A[SYNTHETIC, Splitter:B:87:0x0164] */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void executeSqlBatchStatement(java.lang.String r23, org.json.JSONArray r24, org.json.JSONArray r25) {
        /*
            r22 = this;
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb
            r19 = r0
            if (r19 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            r18 = 0
            r12 = 0
            r13 = 0
            java.lang.String r6 = "unknown"
            r4 = 0
            r11 = 1
            io.sqlc.SQLiteAndroidDatabase$QueryType r15 = getQueryType(r23)     // Catch:{ Exception -> 0x0234 }
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.update     // Catch:{ Exception -> 0x0234 }
            r0 = r19
            if (r15 == r0) goto L_0x0022
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.delete     // Catch:{ Exception -> 0x0234 }
            r0 = r19
            if (r15 != r0) goto L_0x03f5
        L_0x0022:
            boolean r19 = isPostHoneycomb     // Catch:{ Exception -> 0x0234 }
            if (r19 == 0) goto L_0x005f
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ Exception -> 0x0234 }
            r19 = r0
            r0 = r19
            r1 = r23
            android.database.sqlite.SQLiteStatement r10 = r0.compileStatement(r1)     // Catch:{ Exception -> 0x0234 }
            if (r24 == 0) goto L_0x003d
            r0 = r22
            r1 = r24
            r0.bindArgsToStatement(r10, r1)     // Catch:{ Exception -> 0x0234 }
        L_0x003d:
            r17 = -1
            int r17 = r10.executeUpdateDelete()     // Catch:{ SQLiteConstraintException -> 0x01af, SQLiteException -> 0x01eb, Exception -> 0x0212 }
            r11 = 0
        L_0x0044:
            r10.close()     // Catch:{ Exception -> 0x0234 }
            r19 = -1
            r0 = r17
            r1 = r19
            if (r0 == r1) goto L_0x005f
            org.json.JSONObject r14 = new org.json.JSONObject     // Catch:{ Exception -> 0x0234 }
            r14.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "rowsAffected"
            r0 = r19
            r1 = r17
            r14.put(r0, r1)     // Catch:{ Exception -> 0x03de }
            r13 = r14
        L_0x005f:
            if (r11 == 0) goto L_0x03f5
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ Exception -> 0x0234 }
            r19 = r0
            r0 = r22
            r1 = r23
            r2 = r24
            r3 = r19
            int r18 = r0.countRowsAffectedCompat(r15, r1, r2, r3)     // Catch:{ Exception -> 0x0234 }
            r12 = 1
            r14 = r13
        L_0x0075:
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.insert     // Catch:{ Exception -> 0x03de }
            r0 = r19
            if (r15 != r0) goto L_0x00bc
            if (r24 == 0) goto L_0x00bc
            r11 = 0
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ Exception -> 0x03de }
            r19 = r0
            r0 = r19
            r1 = r23
            android.database.sqlite.SQLiteStatement r10 = r0.compileStatement(r1)     // Catch:{ Exception -> 0x03de }
            r0 = r22
            r1 = r24
            r0.bindArgsToStatement(r10, r1)     // Catch:{ Exception -> 0x03de }
            r8 = -1
            long r8 = r10.executeInsert()     // Catch:{ SQLiteConstraintException -> 0x03ee, SQLiteException -> 0x02a3 }
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ SQLiteConstraintException -> 0x03ee, SQLiteException -> 0x02a3 }
            r13.<init>()     // Catch:{ SQLiteConstraintException -> 0x03ee, SQLiteException -> 0x02a3 }
            r20 = -1
            int r19 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r19 == 0) goto L_0x025a
            java.lang.String r19 = "insertId"
            r0 = r19
            r13.put(r0, r8)     // Catch:{ SQLiteConstraintException -> 0x0268, SQLiteException -> 0x03eb }
            java.lang.String r19 = "rowsAffected"
            r20 = 1
            r0 = r19
            r1 = r20
            r13.put(r0, r1)     // Catch:{ SQLiteConstraintException -> 0x0268, SQLiteException -> 0x03eb }
        L_0x00b8:
            r10.close()     // Catch:{ Exception -> 0x0234 }
            r14 = r13
        L_0x00bc:
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.begin     // Catch:{ Exception -> 0x03de }
            r0 = r19
            if (r15 != r0) goto L_0x00e6
            r11 = 0
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ SQLiteException -> 0x02ca }
            r19 = r0
            r19.beginTransaction()     // Catch:{ SQLiteException -> 0x02ca }
            r19 = 1
            r0 = r19
            r1 = r22
            r1.isTransactionActive = r0     // Catch:{ SQLiteException -> 0x02ca }
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ SQLiteException -> 0x02ca }
            r13.<init>()     // Catch:{ SQLiteException -> 0x02ca }
            java.lang.String r19 = "rowsAffected"
            r20 = 0
            r0 = r19
            r1 = r20
            r13.put(r0, r1)     // Catch:{ SQLiteException -> 0x03e8 }
            r14 = r13
        L_0x00e6:
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.commit     // Catch:{ Exception -> 0x03de }
            r0 = r19
            if (r15 != r0) goto L_0x0119
            r11 = 0
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ SQLiteException -> 0x02f2 }
            r19 = r0
            r19.setTransactionSuccessful()     // Catch:{ SQLiteException -> 0x02f2 }
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ SQLiteException -> 0x02f2 }
            r19 = r0
            r19.endTransaction()     // Catch:{ SQLiteException -> 0x02f2 }
            r19 = 0
            r0 = r19
            r1 = r22
            r1.isTransactionActive = r0     // Catch:{ SQLiteException -> 0x02f2 }
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ SQLiteException -> 0x02f2 }
            r13.<init>()     // Catch:{ SQLiteException -> 0x02f2 }
            java.lang.String r19 = "rowsAffected"
            r20 = 0
            r0 = r19
            r1 = r20
            r13.put(r0, r1)     // Catch:{ SQLiteException -> 0x03e5 }
            r14 = r13
        L_0x0119:
            io.sqlc.SQLiteAndroidDatabase$QueryType r19 = p005io.sqlc.SQLiteAndroidDatabase.QueryType.rollback     // Catch:{ Exception -> 0x03de }
            r0 = r19
            if (r15 != r0) goto L_0x03f2
            r11 = 0
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ SQLiteException -> 0x031a }
            r19 = r0
            r19.endTransaction()     // Catch:{ SQLiteException -> 0x031a }
            r19 = 0
            r0 = r19
            r1 = r22
            r1.isTransactionActive = r0     // Catch:{ SQLiteException -> 0x031a }
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ SQLiteException -> 0x031a }
            r13.<init>()     // Catch:{ SQLiteException -> 0x031a }
            java.lang.String r19 = "rowsAffected"
            r20 = 0
            r0 = r19
            r1 = r20
            r13.put(r0, r1)     // Catch:{ SQLiteException -> 0x03e2 }
        L_0x0142:
            if (r11 == 0) goto L_0x0162
            r0 = r22
            android.database.sqlite.SQLiteDatabase r0 = r0.mydb     // Catch:{ SQLiteConstraintException -> 0x0341, SQLiteException -> 0x037c }
            r19 = r0
            r0 = r22
            r1 = r19
            r2 = r23
            r3 = r24
            org.json.JSONObject r13 = r0.executeSqlStatementQuery(r1, r2, r3)     // Catch:{ SQLiteConstraintException -> 0x0341, SQLiteException -> 0x037c }
        L_0x0156:
            if (r12 == 0) goto L_0x0162
            java.lang.String r19 = "rowsAffected"
            r0 = r19
            r1 = r18
            r13.put(r0, r1)     // Catch:{ Exception -> 0x0234 }
        L_0x0162:
            if (r13 == 0) goto L_0x03a2
            org.json.JSONObject r16 = new org.json.JSONObject     // Catch:{ JSONException -> 0x018b }
            r16.<init>()     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "type"
            java.lang.String r20 = "success"
            r0 = r16
            r1 = r19
            r2 = r20
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "result"
            r0 = r16
            r1 = r19
            r0.put(r1, r13)     // Catch:{ JSONException -> 0x018b }
            r0 = r25
            r1 = r16
            r0.put(r1)     // Catch:{ JSONException -> 0x018b }
            goto L_0x0008
        L_0x018b:
            r7 = move-exception
            r7.printStackTrace()
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "SQLiteAndroidDatabase.executeSql[Batch](): Error="
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r21 = r7.getMessage()
            java.lang.StringBuilder r20 = r20.append(r21)
            java.lang.String r20 = r20.toString()
            android.util.Log.v(r19, r20)
            goto L_0x0008
        L_0x01af:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r19.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = "constraint failure: "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r19.toString()     // Catch:{ Exception -> 0x0234 }
            r4 = 6
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteStatement.executeUpdateDelete(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            r11 = 0
            goto L_0x0044
        L_0x01eb:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteStatement.executeUpdateDelete(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            r11 = 0
            goto L_0x0044
        L_0x0212:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteStatement.executeUpdateDelete(): runtime error (fallback to old API): "
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x0044
        L_0x0234:
            r7 = move-exception
        L_0x0235:
            r7.printStackTrace()
            java.lang.String r6 = r7.getMessage()
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder
            r20.<init>()
            java.lang.String r21 = "SQLiteAndroidDatabase.executeSql[Batch](): Error="
            java.lang.StringBuilder r20 = r20.append(r21)
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)
            java.lang.String r20 = r20.toString()
            android.util.Log.v(r19, r20)
            goto L_0x0162
        L_0x025a:
            java.lang.String r19 = "rowsAffected"
            r20 = 0
            r0 = r19
            r1 = r20
            r13.put(r0, r1)     // Catch:{ SQLiteConstraintException -> 0x0268, SQLiteException -> 0x03eb }
            goto L_0x00b8
        L_0x0268:
            r7 = move-exception
        L_0x0269:
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r19.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = "constraint failure: "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r19.toString()     // Catch:{ Exception -> 0x0234 }
            r4 = 6
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteDatabase.executeInsert(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x00b8
        L_0x02a3:
            r7 = move-exception
            r13 = r14
        L_0x02a5:
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteDatabase.executeInsert(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x00b8
        L_0x02ca:
            r7 = move-exception
            r13 = r14
        L_0x02cc:
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteDatabase.beginTransaction(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            r14 = r13
            goto L_0x00e6
        L_0x02f2:
            r7 = move-exception
            r13 = r14
        L_0x02f4:
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteDatabase.setTransactionSuccessful/endTransaction(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            r14 = r13
            goto L_0x0119
        L_0x031a:
            r7 = move-exception
            r13 = r14
        L_0x031c:
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "SQLiteDatabase.endTransaction(): Error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x0142
        L_0x0341:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r19.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = "constraint failure: "
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.StringBuilder r19 = r19.append(r20)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r19.toString()     // Catch:{ Exception -> 0x0234 }
            r4 = 6
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "Raw query error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x0156
        L_0x037c:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r6 = r7.getMessage()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r19 = "executeSqlBatch"
            java.lang.StringBuilder r20 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0234 }
            r20.<init>()     // Catch:{ Exception -> 0x0234 }
            java.lang.String r21 = "Raw query error="
            java.lang.StringBuilder r20 = r20.append(r21)     // Catch:{ Exception -> 0x0234 }
            r0 = r20
            java.lang.StringBuilder r20 = r0.append(r6)     // Catch:{ Exception -> 0x0234 }
            java.lang.String r20 = r20.toString()     // Catch:{ Exception -> 0x0234 }
            android.util.Log.v(r19, r20)     // Catch:{ Exception -> 0x0234 }
            goto L_0x0156
        L_0x03a2:
            org.json.JSONObject r16 = new org.json.JSONObject     // Catch:{ JSONException -> 0x018b }
            r16.<init>()     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "type"
            java.lang.String r20 = "error"
            r0 = r16
            r1 = r19
            r2 = r20
            r0.put(r1, r2)     // Catch:{ JSONException -> 0x018b }
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ JSONException -> 0x018b }
            r5.<init>()     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "message"
            r0 = r19
            r5.put(r0, r6)     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "code"
            r0 = r19
            r5.put(r0, r4)     // Catch:{ JSONException -> 0x018b }
            java.lang.String r19 = "result"
            r0 = r16
            r1 = r19
            r0.put(r1, r5)     // Catch:{ JSONException -> 0x018b }
            r0 = r25
            r1 = r16
            r0.put(r1)     // Catch:{ JSONException -> 0x018b }
            goto L_0x0008
        L_0x03de:
            r7 = move-exception
            r13 = r14
            goto L_0x0235
        L_0x03e2:
            r7 = move-exception
            goto L_0x031c
        L_0x03e5:
            r7 = move-exception
            goto L_0x02f4
        L_0x03e8:
            r7 = move-exception
            goto L_0x02cc
        L_0x03eb:
            r7 = move-exception
            goto L_0x02a5
        L_0x03ee:
            r7 = move-exception
            r13 = r14
            goto L_0x0269
        L_0x03f2:
            r13 = r14
            goto L_0x0142
        L_0x03f5:
            r14 = r13
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: p005io.sqlc.SQLiteAndroidDatabase.executeSqlBatchStatement(java.lang.String, org.json.JSONArray, org.json.JSONArray):void");
    }

    private final int countRowsAffectedCompat(QueryType queryType, String query, JSONArray json_params, SQLiteDatabase mydb2) throws JSONException {
        Matcher whereMatcher = WHERE_CLAUSE.matcher(query);
        String where = "";
        for (int pos = 0; whereMatcher.find(pos); pos = whereMatcher.start(1)) {
            where = " WHERE " + whereMatcher.group(1);
        }
        int numQuestionMarks = 0;
        for (int j = 0; j < where.length(); j++) {
            if (where.charAt(j) == '?') {
                numQuestionMarks++;
            }
        }
        JSONArray subParams = null;
        if (json_params != null) {
            JSONArray origArray = json_params;
            subParams = new JSONArray();
            int startPos = origArray.length() - numQuestionMarks;
            for (int j2 = startPos; j2 < origArray.length(); j2++) {
                subParams.put(j2 - startPos, origArray.get(j2));
            }
        }
        if (queryType == QueryType.update) {
            Matcher tableMatcher = UPDATE_TABLE_NAME.matcher(query);
            if (tableMatcher.find()) {
                try {
                    SQLiteStatement statement = mydb2.compileStatement("SELECT count(*) FROM " + tableMatcher.group(1) + where);
                    if (subParams != null) {
                        bindArgsToStatement(statement, subParams);
                    }
                    return (int) statement.simpleQueryForLong();
                } catch (Exception e) {
                    Log.e(SQLiteAndroidDatabase.class.getSimpleName(), "uncaught", e);
                }
            }
        } else {
            Matcher tableMatcher2 = DELETE_TABLE_NAME.matcher(query);
            if (tableMatcher2.find()) {
                try {
                    SQLiteStatement statement2 = mydb2.compileStatement("SELECT count(*) FROM " + tableMatcher2.group(1) + where);
                    bindArgsToStatement(statement2, subParams);
                    return (int) statement2.simpleQueryForLong();
                } catch (Exception e2) {
                    Log.e(SQLiteAndroidDatabase.class.getSimpleName(), "uncaught", e2);
                }
            }
        }
        return 0;
    }

    private void bindArgsToStatement(SQLiteStatement myStatement, JSONArray sqlArgs) throws JSONException {
        for (int i = 0; i < sqlArgs.length(); i++) {
            if ((sqlArgs.get(i) instanceof Float) || (sqlArgs.get(i) instanceof Double)) {
                myStatement.bindDouble(i + 1, sqlArgs.getDouble(i));
            } else if (sqlArgs.get(i) instanceof Number) {
                myStatement.bindLong(i + 1, sqlArgs.getLong(i));
            } else if (sqlArgs.isNull(i)) {
                myStatement.bindNull(i + 1);
            } else {
                myStatement.bindString(i + 1, sqlArgs.getString(i));
            }
        }
    }

    private JSONObject executeSqlStatementQuery(SQLiteDatabase mydb2, String query, JSONArray paramsAsJson) throws Exception {
        JSONObject rowsResult = new JSONObject();
        try {
            String[] params = new String[paramsAsJson.length()];
            for (int j = 0; j < paramsAsJson.length(); j++) {
                if (paramsAsJson.isNull(j)) {
                    params[j] = "";
                } else {
                    params[j] = paramsAsJson.getString(j);
                }
            }
            Cursor cur = mydb2.rawQuery(query, params);
            if (cur != null && cur.moveToFirst()) {
                JSONArray rowsArrayResult = new JSONArray();
                String str = "";
                int colCount = cur.getColumnCount();
                do {
                    JSONObject row = new JSONObject();
                    int i = 0;
                    while (i < colCount) {
                        try {
                            String key = cur.getColumnName(i);
                            if (isPostHoneycomb) {
                                try {
                                    bindPostHoneycomb(row, key, cur, i);
                                } catch (Exception e) {
                                    bindPreHoneycomb(row, key, cur, i);
                                }
                            } else {
                                bindPreHoneycomb(row, key, cur, i);
                            }
                            i++;
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                    rowsArrayResult.put(row);
                } while (cur.moveToNext());
                try {
                    rowsResult.put("rows", rowsArrayResult);
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
            if (cur != null) {
                cur.close();
            }
            return rowsResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v("executeSqlBatch", "SQLiteAndroidDatabase.executeSql[Batch](): Error=" + ex.getMessage());
            throw ex;
        }
    }

    @SuppressLint({"NewApi"})
    private void bindPostHoneycomb(JSONObject row, String key, Cursor cur, int i) throws JSONException {
        switch (cur.getType(i)) {
            case 0:
                row.put(key, JSONObject.NULL);
                return;
            case 1:
                row.put(key, cur.getLong(i));
                return;
            case 2:
                row.put(key, cur.getDouble(i));
                return;
            default:
                row.put(key, cur.getString(i));
                return;
        }
    }

    private void bindPreHoneycomb(JSONObject row, String key, Cursor cursor, int i) throws JSONException {
        CursorWindow cursorWindow = ((SQLiteCursor) cursor).getWindow();
        int pos = cursor.getPosition();
        if (cursorWindow.isNull(pos, i)) {
            row.put(key, JSONObject.NULL);
        } else if (cursorWindow.isLong(pos, i)) {
            row.put(key, cursor.getLong(i));
        } else if (cursorWindow.isFloat(pos, i)) {
            row.put(key, cursor.getDouble(i));
        } else {
            row.put(key, cursor.getString(i));
        }
    }

    static QueryType getQueryType(String query) {
        Matcher matcher = FIRST_WORD.matcher(query);
        if (matcher.find()) {
            try {
                String first = matcher.group(1);
                if (first.length() != 0) {
                    return QueryType.valueOf(first.toLowerCase(Locale.ENGLISH));
                }
                throw new RuntimeException("query not found");
            } catch (IllegalArgumentException e) {
                return QueryType.other;
            }
        } else {
            throw new RuntimeException("query not found");
        }
    }
}
