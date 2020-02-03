package p005io.sqlc;

import android.util.Log;
import com.facebook.internal.NativeProtocol;
import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: io.sqlc.SQLitePlugin */
public class SQLitePlugin extends CordovaPlugin {
    static Map<String, DBRunner> dbrmap = new ConcurrentHashMap();

    /* renamed from: io.sqlc.SQLitePlugin$Action */
    private enum Action {
        echoStringValue,
        open,
        close,
        delete,
        executeSqlBatch,
        backgroundExecuteSqlBatch
    }

    /* renamed from: io.sqlc.SQLitePlugin$DBQuery */
    private final class DBQuery {
        final CallbackContext cbc;
        final boolean close;
        final boolean delete;
        final JSONArray[] jsonparams;
        final String[] queries;
        final boolean stop;

        DBQuery(String[] myqueries, JSONArray[] params, CallbackContext c) {
            this.stop = false;
            this.close = false;
            this.delete = false;
            this.queries = myqueries;
            this.jsonparams = params;
            this.cbc = c;
        }

        DBQuery(boolean delete2, CallbackContext cbc2) {
            this.stop = true;
            this.close = true;
            this.delete = delete2;
            this.queries = null;
            this.jsonparams = null;
            this.cbc = cbc2;
        }

        DBQuery() {
            this.stop = true;
            this.close = false;
            this.delete = false;
            this.queries = null;
            this.jsonparams = null;
            this.cbc = null;
        }
    }

    /* renamed from: io.sqlc.SQLitePlugin$DBRunner */
    private class DBRunner implements Runnable {
        private boolean bugWorkaround;
        final String dbname;
        SQLiteAndroidDatabase mydb;
        private boolean oldImpl;
        final CallbackContext openCbc;

        /* renamed from: q */
        final BlockingQueue<DBQuery> f65q;

        DBRunner(String dbname2, JSONObject options, CallbackContext cbc) {
            this.dbname = dbname2;
            this.oldImpl = options.has("androidOldDatabaseImplementation");
            Log.v(SQLitePlugin.class.getSimpleName(), "Android db implementation: built-in android.database.sqlite package");
            this.bugWorkaround = this.oldImpl && options.has("androidBugWorkaround");
            if (this.bugWorkaround) {
                Log.v(SQLitePlugin.class.getSimpleName(), "Android db closing/locking workaround applied");
            }
            this.f65q = new LinkedBlockingQueue();
            this.openCbc = cbc;
        }

        public void run() {
            try {
                this.mydb = SQLitePlugin.this.openDatabase(this.dbname, this.openCbc, this.oldImpl);
                DBQuery dbq = null;
                try {
                    Object take = this.f65q.take();
                    while (true) {
                        dbq = (DBQuery) take;
                        if (dbq.stop) {
                            break;
                        }
                        this.mydb.executeSqlBatch(dbq.queries, dbq.jsonparams, dbq.cbc);
                        if (this.bugWorkaround && dbq.queries.length == 1 && dbq.queries[0] == "COMMIT") {
                            this.mydb.bugWorkaround();
                        }
                        take = this.f65q.take();
                    }
                } catch (Exception e) {
                    Log.e(SQLitePlugin.class.getSimpleName(), "unexpected error", e);
                }
                if (dbq != null && dbq.close) {
                    try {
                        SQLitePlugin.this.closeDatabaseNow(this.dbname);
                        SQLitePlugin.dbrmap.remove(this.dbname);
                        if (!dbq.delete) {
                            dbq.cbc.success();
                            return;
                        }
                        try {
                            if (SQLitePlugin.this.deleteDatabaseNow(this.dbname)) {
                                dbq.cbc.success();
                            } else {
                                dbq.cbc.error("couldn't delete database");
                            }
                        } catch (Exception e2) {
                            Log.e(SQLitePlugin.class.getSimpleName(), "couldn't delete database", e2);
                            dbq.cbc.error("couldn't delete database: " + e2);
                        }
                    } catch (Exception e3) {
                        Log.e(SQLitePlugin.class.getSimpleName(), "couldn't close database", e3);
                        if (dbq.cbc != null) {
                            dbq.cbc.error("couldn't close database: " + e3);
                        }
                    }
                }
            } catch (Exception e4) {
                Log.e(SQLitePlugin.class.getSimpleName(), "unexpected error, stopping db thread", e4);
                SQLitePlugin.dbrmap.remove(this.dbname);
            }
        }
    }

    public boolean execute(String actionAsString, JSONArray args, CallbackContext cbc) {
        boolean z = false;
        try {
            try {
                return executeAndPossiblyThrow(Action.valueOf(actionAsString), args, cbc);
            } catch (JSONException e) {
                Log.e(SQLitePlugin.class.getSimpleName(), "unexpected error", e);
                return z;
            }
        } catch (IllegalArgumentException e2) {
            Log.e(SQLitePlugin.class.getSimpleName(), "unexpected error", e2);
            return z;
        }
    }

    private boolean executeAndPossiblyThrow(Action action, JSONArray args, CallbackContext cbc) throws JSONException {
        switch (action) {
            case echoStringValue:
                cbc.success(args.getJSONObject(0).getString("value"));
                break;
            case open:
                JSONObject o = args.getJSONObject(0);
                startDatabase(o.getString("name"), o, cbc);
                break;
            case close:
                closeDatabase(args.getJSONObject(0).getString("path"), cbc);
                break;
            case delete:
                deleteDatabase(args.getJSONObject(0).getString("path"), cbc);
                break;
            case executeSqlBatch:
            case backgroundExecuteSqlBatch:
                JSONObject allargs = args.getJSONObject(0);
                String dbname = allargs.getJSONObject("dbargs").getString("dbname");
                JSONArray txargs = allargs.getJSONArray("executes");
                if (!txargs.isNull(0)) {
                    int len = txargs.length();
                    String[] queries = new String[len];
                    JSONArray[] jsonparams = new JSONArray[len];
                    for (int i = 0; i < len; i++) {
                        JSONObject a = txargs.getJSONObject(i);
                        queries[i] = a.getString("sql");
                        jsonparams[i] = a.getJSONArray(NativeProtocol.WEB_DIALOG_PARAMS);
                    }
                    DBQuery q = new DBQuery(queries, jsonparams, cbc);
                    DBRunner r = (DBRunner) dbrmap.get(dbname);
                    if (r == null) {
                        cbc.error("INTERNAL PLUGIN ERROR: database not open");
                        break;
                    } else {
                        try {
                            r.f65q.put(q);
                            break;
                        } catch (Exception e) {
                            Log.e(SQLitePlugin.class.getSimpleName(), "couldn't add to queue", e);
                            cbc.error("INTERNAL PLUGIN ERROR: couldn't add to queue");
                            break;
                        }
                    }
                } else {
                    cbc.error("INTERNAL PLUGIN ERROR: missing executes list");
                    break;
                }
        }
        return true;
    }

    public void onDestroy() {
        while (!dbrmap.isEmpty()) {
            String dbname = (String) dbrmap.keySet().iterator().next();
            closeDatabaseNow(dbname);
            try {
                ((DBRunner) dbrmap.get(dbname)).f65q.put(new DBQuery());
            } catch (Exception e) {
                Log.e(SQLitePlugin.class.getSimpleName(), "couldn't stop db thread", e);
            }
            dbrmap.remove(dbname);
        }
    }

    private void startDatabase(String dbname, JSONObject options, CallbackContext cbc) {
        if (((DBRunner) dbrmap.get(dbname)) != null) {
            cbc.error("INTERNAL ERROR: database already open for db name: " + dbname);
            return;
        }
        DBRunner r = new DBRunner(dbname, options, cbc);
        dbrmap.put(dbname, r);
        this.cordova.getThreadPool().execute(r);
    }

    /* access modifiers changed from: private */
    public SQLiteAndroidDatabase openDatabase(String dbname, CallbackContext cbc, boolean old_impl) throws Exception {
        try {
            File dbfile = this.cordova.getActivity().getDatabasePath(dbname);
            if (!dbfile.exists()) {
                dbfile.getParentFile().mkdirs();
            }
            Log.v("info", "Open sqlite db: " + dbfile.getAbsolutePath());
            SQLiteAndroidDatabase mydb = old_impl ? new SQLiteAndroidDatabase() : new SQLiteConnectorDatabase();
            mydb.open(dbfile);
            if (cbc != null) {
                cbc.success();
            }
            return mydb;
        } catch (Exception e) {
            if (cbc != null) {
                cbc.error("can't open database " + e);
            }
            throw e;
        }
    }

    private void closeDatabase(String dbname, CallbackContext cbc) {
        DBRunner r = (DBRunner) dbrmap.get(dbname);
        if (r != null) {
            try {
                r.f65q.put(new DBQuery(false, cbc));
            } catch (Exception e) {
                if (cbc != null) {
                    cbc.error("couldn't close database" + e);
                }
                Log.e(SQLitePlugin.class.getSimpleName(), "couldn't close database", e);
            }
        } else if (cbc != null) {
            cbc.success();
        }
    }

    /* access modifiers changed from: private */
    public void closeDatabaseNow(String dbname) {
        DBRunner r = (DBRunner) dbrmap.get(dbname);
        if (r != null) {
            SQLiteAndroidDatabase mydb = r.mydb;
            if (mydb != null) {
                mydb.closeDatabaseNow();
            }
        }
    }

    private void deleteDatabase(String dbname, CallbackContext cbc) {
        DBRunner r = (DBRunner) dbrmap.get(dbname);
        if (r != null) {
            try {
                r.f65q.put(new DBQuery(true, cbc));
            } catch (Exception e) {
                if (cbc != null) {
                    cbc.error("couldn't close database" + e);
                }
                Log.e(SQLitePlugin.class.getSimpleName(), "couldn't close database", e);
            }
        } else if (deleteDatabaseNow(dbname)) {
            cbc.success();
        } else {
            cbc.error("couldn't delete database");
        }
    }

    /* access modifiers changed from: private */
    public boolean deleteDatabaseNow(String dbname) {
        try {
            return this.cordova.getActivity().deleteDatabase(this.cordova.getActivity().getDatabasePath(dbname).getAbsolutePath());
        } catch (Exception e) {
            Log.e(SQLitePlugin.class.getSimpleName(), "couldn't delete database", e);
            return false;
        }
    }
}
