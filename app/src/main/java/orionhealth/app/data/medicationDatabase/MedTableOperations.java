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
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedTableInfo;

/**
 * Created by bill on 11/04/16.
 */
public final class MedTableOperations {
	private static MedTableOperations medTableOperations;
	private FhirServices fhirServices;

	private MedTableOperations(){
		fhirServices = FhirServices.getFhirServices();
	}

	public static MedTableOperations getInstance(){
		if (medTableOperations == null){
			medTableOperations = new MedTableOperations();
			return medTableOperations;
		}else{
			return medTableOperations;
		}
	}

	public void addToMedTable(Context context, MedicationStatement medStatement) {
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();

		String jsonStringMed = FhirServices.getFhirServices().toJsonString(medStatement);
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, jsonStringMed);
		database.insert(MedTableInfo.TABLE_NAME, null, cv);
	}

	public static Cursor getAllRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
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
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo.COLUMN_NAME_JSON_STRING
		};

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, MedTableInfo._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_JSON_STRING));
			FhirContext fhirContext = fhirServices.getFhirContextInstance();
			MedicationStatement medStatement = (MedicationStatement) fhirContext.newJsonParser().parseResource(jsonMedString);
			return medStatement;
		}
		return null;
	};

	public void removeMedication(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();
		String selection = MedTableInfo._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		db.delete(MedTableInfo.TABLE_NAME, selection, selectionArgs);
	}

	public void updateMedication(Context context, int id, MedicationStatement updatedMedStatement){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();

		FhirContext fhirContext = fhirServices.getFhirContextInstance();
		String updatedJsonMedString = fhirContext.newJsonParser().encodeResourceToString(updatedMedStatement);
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, updatedJsonMedString);
		String selection = MedTableInfo._ID + " = ?";
		String[] selectionArgs = new String[]{String.valueOf(id)};
		db.update(MedTableInfo.TABLE_NAME, cv, selection, selectionArgs);
	}

	public void clearMedTable(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getsInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();
		db.delete(MedTableInfo.TABLE_NAME, null, null);
	}
}
