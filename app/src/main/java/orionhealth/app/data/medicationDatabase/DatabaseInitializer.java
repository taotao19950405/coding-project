//       Description:
//			Create instance in databse
//		 @ Author:  Bill
//			@Review: Lu
//			@Reviewer: 19 Apr 2016
package orionhealth.app.data.medicationDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedTableInfo;
import orionhealth.app.data.medicationDatabase.DatabaseContract.CondTableInfo;

/**
 * Created by bill on 8/04/16.
 */
public class DatabaseInitializer extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MedTableInfo.TABLE_NAME + " (" +
                    MedTableInfo._ID + " INTEGER PRIMARY KEY," +
                    MedTableInfo.COLUMN_NAME_JSON_STRING + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_ENTRIES_COND =
            "CREATE TABLE " + CondTableInfo.TABLE_NAME + " (" +
                    CondTableInfo._ID + " INTEGER PRIMARY KEY," +
                    CondTableInfo.COLUMN_NAME_JSON_STRING + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MedTableInfo.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_COND =
            "DROP TABLE IF EXISTS " + CondTableInfo.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Main.db";

    private static DatabaseInitializer sInstance;

    private DatabaseInitializer(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseInitializer getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseInitializer(context.getApplicationContext());
        }
        return sInstance;
    }

    ;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES_COND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES_COND);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}

