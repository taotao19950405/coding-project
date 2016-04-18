//       Description:
//		 @author:  Bill
package orionhealth.app.medicationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import orionhealth.app.dataModels.Medication;

/**
 * Created by bill on 11/04/16.
 */
public final class MedTableOperations {

	public static void addToMedTable(Context context, Medication med) {
		DatabaseOpener dbo = DatabaseOpener.getsInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DatabaseContract.MedTableInfo.COLUMN_NAME_NAME, med.getName());
		cv.put(DatabaseContract.MedTableInfo.COLUMN_NAME_DOSAGE, med.getDosage());
		database.insert(DatabaseContract.MedTableInfo.TABLE_NAME, null, cv);
	}

	public static Cursor getAllRows(Context context){
		DatabaseOpener dbo = DatabaseOpener.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  DatabaseContract.MedTableInfo._ID,
		  DatabaseContract.MedTableInfo.COLUMN_NAME_NAME,
		  DatabaseContract.MedTableInfo.COLUMN_NAME_DOSAGE
		};

		String sortOrder =
		  DatabaseContract.MedTableInfo._ID + " ASC";

		Cursor cursor = db.query(
		  DatabaseContract.MedTableInfo.TABLE_NAME, projection, null, null, null, null, sortOrder
		);
		return cursor;
	}

	public static Medication getMedication(Context context, int id){
		DatabaseOpener dbo = DatabaseOpener.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  DatabaseContract.MedTableInfo.COLUMN_NAME_NAME,
		  DatabaseContract.MedTableInfo.COLUMN_NAME_DOSAGE
		};

		Cursor cursor = db.query(
		  DatabaseContract.MedTableInfo.TABLE_NAME, projection, DatabaseContract.MedTableInfo._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_NAME));
			int dosage = cursor.getInt(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_DOSAGE));
			return new Medication(name, dosage);
		}
		return null;
	};

	public static void removeMedication(Context context, int id){
		DatabaseOpener dbo = DatabaseOpener.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();
		String selection = DatabaseContract.MedTableInfo._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(DatabaseContract.MedTableInfo.TABLE_NAME, selection, selectionArgs);
	}

	public static void updateMedication(Context context, int id, Medication updatedMed){
		DatabaseOpener dbo = DatabaseOpener.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		// Tao see if you can finish this method
	}
}
