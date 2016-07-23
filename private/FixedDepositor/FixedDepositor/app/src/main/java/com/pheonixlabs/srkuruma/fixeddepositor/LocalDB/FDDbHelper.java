package com.pheonixlabs.srkuruma.fixeddepositor.LocalDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;

/**
 * Created by srkuruma on 5/15/2016.
 */
public class FDDbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FixedDeposits.db";

    public static final String TEXT_TYPE = " TEXT";
    public static final String DATETIME_TYPE = " DATETIME";
    public static final String INT_TYPE = " INT";
    public static final String DOUBLE_TYPE = " DOUBLE";
    public static final String COMMA_SEP = ",";

    public FDDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FDEntity.SQL_CREATE_FDENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FDEntity.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
