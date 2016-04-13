//       Description:
//		 @author:  Bill
package orionhealth.app.medicationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import orionhealth.app.dataModels.Medication;
import orionhealth.app.medicationDatabase.DatabaseContract.*;

/**
 * Created by bill on 8/04/16.
 */
public class DatabaseOpener extends SQLiteOpenHelper {

	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES =
		"CREATE TABLE " + MedTableInfo.TABLE_NAME + " (" +
			MedTableInfo._ID + " INTEGER PRIMARY KEY," +
			MedTableInfo.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
			MedTableInfo.COLUMN_NAME_DOSAGE + INTEGER_TYPE +
			" )";

	private static final String SQL_DELETE_ENTRIES =
		"DROP TABLE IF EXISTS " + MedTableInfo.TABLE_NAME;

	// If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Main.db";

	public DatabaseOpener(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}

