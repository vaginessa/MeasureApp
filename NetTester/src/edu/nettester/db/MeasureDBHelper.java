package edu.nettester.db;

import edu.nettester.db.MeasureContract.MeasureLog;
import edu.nettester.util.CommonMethod;
import edu.nettester.util.Constant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MeasureDBHelper extends SQLiteOpenHelper implements Constant {

    /**
     * we will not change the db version
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MeasureDB.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_MLOG =
        "CREATE TABLE " + MeasureLog.TABLE_NAME + " (" +
        MeasureLog._ID + " INTEGER PRIMARY KEY," +
        MeasureLog.MTIME + TEXT_TYPE + COMMA_SEP +
        MeasureLog.MUID + TEXT_TYPE + COMMA_SEP +
        MeasureLog.MID + TEXT_TYPE + COMMA_SEP +
        MeasureLog.UPFLG + " BOOLEAN " + COMMA_SEP +
        MeasureLog.M_NET_INFO + TEXT_TYPE + COMMA_SEP +
        MeasureLog.M_LOC_INFO + TEXT_TYPE + COMMA_SEP +
        MeasureLog.M_TAR_SERVER + TEXT_TYPE + COMMA_SEP +
        MeasureLog.AVG_RTT + TEXT_TYPE + COMMA_SEP +
        MeasureLog.MEDIAN_RTT + TEXT_TYPE + COMMA_SEP +
        MeasureLog.MIN_RTT + TEXT_TYPE + COMMA_SEP +
        MeasureLog.MAX_RTT + TEXT_TYPE + COMMA_SEP +
        MeasureLog.STDV_RTT + TEXT_TYPE + COMMA_SEP +
        MeasureLog.DOWN_TP + TEXT_TYPE + COMMA_SEP +
        MeasureLog.UP_TP + TEXT_TYPE + " DEFAULT '0'" +
        " )";
        
    private static final String SQL_DELETE_MLOG =
        "DROP TABLE IF EXISTS " + MeasureLog.TABLE_NAME;
    
    public MeasureDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MLOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // we have no need to upgrade db
        db.execSQL(SQL_DELETE_MLOG);
        onCreate(db);
    }

    public Cursor fetchAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(
                MeasureLog.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                MeasureLog.MUID + "=? OR " + MeasureLog.MUID + "=?",                                // The columns for the WHERE clause
                new String[] {CommonMethod.M_UID, "0"},                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                MeasureLog.MID + " DESC"                                 // The sort order
                );
        
        return cur;
    }
    
    public int deleteOneRow(Cursor cursor) {
        int result;
        
        long keyid = cursor.getLong(cursor.getColumnIndex(MeasureLog._ID));
        
        SQLiteDatabase db = this.getReadableDatabase();
        result = db.delete(MeasureLog.TABLE_NAME, MeasureLog._ID+"="+keyid, null);
        
        if (DEBUG)
            Log.d(TAG, "Delete "+result+" row.");
        
        return result;
    }
    
    public Cursor fetchOneRow(long keyid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(
                MeasureLog.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                MeasureLog._ID+"="+keyid,           // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
                );
        
        return cur;
    }
    
    public long fetchKeyId(Cursor cursor) {
        long keyid = cursor.getLong(cursor.getColumnIndex(MeasureLog._ID));
        
        return keyid;
    }
}
