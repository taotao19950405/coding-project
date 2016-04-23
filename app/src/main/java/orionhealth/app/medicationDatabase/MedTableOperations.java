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
import orionhealth.app.medicationDatabase.DatabaseContract.MedTableInfo;

/**
 * Created by bill on 11/04/16.
 */
public final class MedTableOperations {

	public static void addToMedTable(Context context, Medication med) {
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(MedTableInfo.COLUMN_NAME_NAME, med.getName());
		cv.put(MedTableInfo.COLUMN_NAME_DOSAGE, med.getDosage());
		database.insert(MedTableInfo.TABLE_NAME, null, cv);
	}

	public static Cursor getAllRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo._ID,
		  MedTableInfo.COLUMN_NAME_NAME,
		  MedTableInfo.COLUMN_NAME_DOSAGE
		};

		String sortOrder =
		  MedTableInfo._ID + " ASC";

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, null, null, null, null, sortOrder
		);
		return cursor;
	}

	public static Medication getMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo.COLUMN_NAME_NAME,
		  MedTableInfo.COLUMN_NAME_DOSAGE
		};

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, MedTableInfo._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_NAME));
			int dosage = cursor.getInt(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_DOSAGE));
			return new Medication(name, dosage);
		}
		return null;
	};

	public static void removeMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();
		String selection = MedTableInfo._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(MedTableInfo.TABLE_NAME, selection, selectionArgs);
	}

	public static void updateMedication(Context context, int id, Medication updatedMed){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(MedTableInfo.COLUMN_NAME_NAME, updatedMed.getName());
		cv.put(MedTableInfo.COLUMN_NAME_DOSAGE, updatedMed.getDosage());
		String selection = MedTableInfo._ID + " = ?";
		String[] selectionArgs = new String[]{String.valueOf(id)};
		db.update(MedTableInfo.TABLE_NAME, cv, selection, selectionArgs);
	}
}
