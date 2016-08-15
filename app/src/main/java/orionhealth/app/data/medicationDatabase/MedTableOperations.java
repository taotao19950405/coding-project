//       Description:
// 			Store data in table
// 			Fetch data from readable table in the format of Sqlite database instance
//			Query the datababse
//		 @author:  Bill
//			@Reviewer: 19 Apr 2016
package orionhealth.app.data.medicationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.data.dataModels.MyMedicationStatement;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedTableInfo;

/**
 * Created by bill on 11/04/16.
 */
public final class MedTableOperations {
	private static MedTableOperations sMedTableOperations;
	private FhirServices mFhirServices;

	private MedTableOperations(){
		mFhirServices = FhirServices.getsFhirServices();
	}

	public static MedTableOperations getInstance(){
		if (sMedTableOperations == null){
			sMedTableOperations = new MedTableOperations();
			return sMedTableOperations;
		}else{
			return sMedTableOperations;
		}
	}

	public int addToMedTable(Context context, MyMedicationStatement myMedStatement) {
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();

		String jsonStringMed =
		  		FhirServices.getsFhirServices().toJsonString(myMedStatement.getFhirMedStatement());
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, jsonStringMed);

		Boolean reminderSet = myMedStatement.getReminderSet();
		cv.put(MedTableInfo.COLUMN_NAME_REMINDER_SET, reminderSet);
		return (int) database.insert(MedTableInfo.TABLE_NAME, null, cv);
	}

	public Cursor getAllRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo._ID,
		  MedTableInfo.COLUMN_NAME_JSON_STRING
		};

		String sortOrder =
		  MedTableInfo._ID + " ASC";

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, null, null, null, null, sortOrder
		);
		return cursor;
	}

	public MedicationStatement getMedicationStatement(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo.COLUMN_NAME_JSON_STRING
		};

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, MedTableInfo._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_JSON_STRING));
			MedicationStatement medStatement = (MedicationStatement) mFhirServices.toResource(jsonMedString);
			return medStatement;
		}
		return null;
	};

	public void removeMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();
		String selection = MedTableInfo._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(MedTableInfo.TABLE_NAME, selection, selectionArgs);
	}

	public void updateMedication(Context context, int id, MedicationStatement updatedMedStatement){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();
		String updatedJsonMedString = mFhirServices.toJsonString(updatedMedStatement);
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, updatedJsonMedString);
		String selection = MedTableInfo._ID + " = ?";
		String[] selectionArgs = new String[]{String.valueOf(id)};
		db.update(MedTableInfo.TABLE_NAME, cv, selection, selectionArgs);
	}

	public void clearMedTable(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();
		db.delete(MedTableInfo.TABLE_NAME, null, null);
	}
}
