package p005io.sqlc;

import android.util.Log;
import com.facebook.GraphResponse;
import com.facebook.share.internal.ShareConstants;
import java.io.File;
import java.sql.SQLException;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import p005io.liteglue.SQLiteConnection;
import p005io.liteglue.SQLiteConnector;
import p005io.liteglue.SQLiteStatement;

/* renamed from: io.sqlc.SQLiteConnectorDatabase */
class SQLiteConnectorDatabase extends SQLiteAndroidDatabase {
    static SQLiteConnector connector = new SQLiteConnector();
    SQLiteConnection mydb;

    SQLiteConnectorDatabase() {
    }

    /* access modifiers changed from: 0000 */
    public void open(File dbFile) throws Exception {
        this.mydb = connector.newSQLiteConnection(dbFile.getAbsolutePath(), 6);
    }

    /* access modifiers changed from: 0000 */
    public void closeDatabaseNow() {
        try {
            if (this.mydb != null) {
                this.mydb.dispose();
            }
        } catch (Exception e) {
            Log.e(SQLitePlugin.class.getSimpleName(), "couldn't close database, ignoring", e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void bugWorkaround() {
    }

    /* access modifiers changed from: 0000 */
    public void executeSqlBatch(String[] queryarr, JSONArray[] jsonparams, CallbackContext cbc) {
        if (this.mydb == null) {
            cbc.error("database has been closed");
            return;
        }
        int len = queryarr.length;
        JSONArray batchResults = new JSONArray();
        for (int i = 0; i < len; i++) {
            JSONObject queryResult = null;
            String errorMessage = "unknown";
            int code = 0;
            try {
                long lastTotal = (long) this.mydb.getTotalChanges();
                queryResult = executeSQLiteStatement(queryarr[i], jsonparams[i], cbc);
                long rowsAffected = ((long) this.mydb.getTotalChanges()) - lastTotal;
                queryResult.put("rowsAffected", rowsAffected);
                if (rowsAffected > 0) {
                    long insertId = this.mydb.getLastInsertRowid();
                    if (insertId > 0) {
                        queryResult.put("insertId", insertId);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                int sqliteErrorCode = ex.getErrorCode();
                errorMessage = ex.getMessage();
                Log.v("executeSqlBatch", "SQLitePlugin.executeSql[Batch](): SQL Error code = " + sqliteErrorCode + " message = " + errorMessage);
                switch (sqliteErrorCode) {
                    case 1:
                        code = 5;
                        break;
                    case 13:
                        code = 4;
                        break;
                    case 19:
                        code = 6;
                        break;
                }
            } catch (JSONException ex2) {
                ex2.printStackTrace();
                errorMessage = ex2.getMessage();
                code = 0;
                Log.e("executeSqlBatch", "SQLitePlugin.executeSql[Batch](): UNEXPECTED JSON Error=" + errorMessage);
            }
            if (queryResult != null) {
                try {
                    JSONObject r = new JSONObject();
                    r.put("type", GraphResponse.SUCCESS_KEY);
                    r.put("result", queryResult);
                    batchResults.put(r);
                } catch (JSONException ex3) {
                    ex3.printStackTrace();
                    Log.e("executeSqlBatch", "SQLitePlugin.executeSql[Batch](): Error=" + ex3.getMessage());
                }
            } else {
                JSONObject r2 = new JSONObject();
                r2.put("type", "error");
                JSONObject er = new JSONObject();
                er.put(ShareConstants.WEB_DIALOG_PARAM_MESSAGE, errorMessage);
                er.put("code", code);
                r2.put("result", er);
                batchResults.put(r2);
            }
        }
        cbc.success(batchResults);
    }

    private JSONObject executeSQLiteStatement(String query, JSONArray paramsAsJson, CallbackContext cbc) throws JSONException, SQLException {
        JSONObject rowsResult = new JSONObject();
        SQLiteStatement myStatement = this.mydb.prepareStatement(query);
        try {
            String[] params = new String[paramsAsJson.length()];
            for (int i = 0; i < paramsAsJson.length(); i++) {
                if (paramsAsJson.isNull(i)) {
                    myStatement.bindNull(i + 1);
                } else {
                    Object p = paramsAsJson.get(i);
                    if ((p instanceof Float) || (p instanceof Double)) {
                        myStatement.bindDouble(i + 1, paramsAsJson.getDouble(i));
                    } else if (p instanceof Number) {
                        myStatement.bindLong(i + 1, paramsAsJson.getLong(i));
                    } else {
                        myStatement.bindTextNativeString(i + 1, paramsAsJson.getString(i));
                    }
                }
            }
            if (myStatement.step()) {
                JSONArray rowsArrayResult = new JSONArray();
                String str = "";
                int colCount = myStatement.getColumnCount();
                do {
                    JSONObject row = new JSONObject();
                    int i2 = 0;
                    while (i2 < colCount) {
                        try {
                            String key = myStatement.getColumnName(i2);
                            switch (myStatement.getColumnType(i2)) {
                                case 1:
                                    row.put(key, myStatement.getColumnLong(i2));
                                    break;
                                case 2:
                                    row.put(key, myStatement.getColumnDouble(i2));
                                    break;
                                case 5:
                                    row.put(key, JSONObject.NULL);
                                    break;
                                default:
                                    row.put(key, myStatement.getColumnTextNativeString(i2));
                                    break;
                            }
                            i2++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    rowsArrayResult.put(row);
                } while (myStatement.step());
                try {
                    rowsResult.put("rows", rowsArrayResult);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            myStatement.dispose();
            return rowsResult;
        } catch (SQLException ex) {
            ex.printStackTrace();
            Log.v("executeSqlBatch", "SQLitePlugin.executeSql[Batch](): Error=" + ex.getMessage());
            myStatement.dispose();
            throw ex;
        } catch (JSONException ex2) {
            ex2.printStackTrace();
            Log.v("executeSqlBatch", "SQLitePlugin.executeSql[Batch](): Error=" + ex2.getMessage());
            myStatement.dispose();
            throw ex2;
        }
    }
}
