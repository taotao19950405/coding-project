//       Description:
// 			Store data in table
// 			Fetch data from readable table in the format of Sqlite database instance
//			Query the datababse
//		 @author:  Bill
//			@Reviewer: 19 Apr 2016
package orionhealth.app.medicationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import orionhealth.app.dataModels.Medication;
import orionhealth.app.medicationDatabase.DatabaseContract.*;

/**
 * Created by bill on 11/04/16.
 */
public final class MedTableOperations {

	public MedTableOperations(){
	}

	public static void addToMedTable(Context context, String jsonStringMed) {
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(MedTableInfo2.COLUMN_NAME_JSON_STRING, jsonStringMed);
		database.insert(MedTableInfo2.TABLE_NAME, null, cv);
	}

	public static Cursor getAllRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo2._ID,
		  MedTableInfo2.COLUMN_NAME_JSON_STRING
		};

		String sortOrder =
		  MedTableInfo2._ID + " ASC";

		Cursor cursor = db.query(
		  MedTableInfo2.TABLE_NAME, projection, null, null, null, null, sortOrder
		);
		return cursor;
	}

	public static String getMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo2.COLUMN_NAME_JSON_STRING
		};

		Cursor cursor = db.query(
		  MedTableInfo2.TABLE_NAME, projection, MedTableInfo2._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(MedTableInfo2.COLUMN_NAME_JSON_STRING));
			return jsonMedString;
		}
		return null;
	};

	public static void removeMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();
		String selection = MedTableInfo2._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(MedTableInfo2.TABLE_NAME, selection, selectionArgs);
	}

	public static void updateMedication(Context context, int id, String updatedJsonMedString){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(MedTableInfo2.COLUMN_NAME_JSON_STRING, updatedJsonMedString);
		String selection = MedTableInfo2._ID + " = ?";
		String[] selectionArgs = new String[]{String.valueOf(id)};
		db.update(MedTableInfo2.TABLE_NAME, cv, selection, selectionArgs);
	}
}
