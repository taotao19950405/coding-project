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
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.data.dataModels.AlarmPackage;
import orionhealth.app.data.dataModels.MyMedication;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedTableInfo;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedReminderTableInfo;

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

	public int addToMedTable(Context context, MyMedication myMedStatement) {
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase database = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();

		String jsonStringMed =
		  		FhirServices.getsFhirServices().toJsonString(myMedStatement.getFhirMedStatement());
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, jsonStringMed);

		Boolean reminderSet = myMedStatement.getReminderSet();
		cv.put(MedTableInfo.COLUMN_NAME_REMINDER_SET, reminderSet);

		AlarmPackage alarmPackage = myMedStatement.getAlarmPackage();
		for (int i = 0; i < alarmPackage.getDailyNumOfAlarms(); i++) {
			long alarmTime = alarmPackage.getAlarmTime() + i * alarmPackage.getIntervalTimeToNextAlarm();
			ContentValues cv2 = new ContentValues();
			cv2.put(MedReminderTableInfo.COLUMN_NAME_MEDICATION, jsonStringMed);
			cv2.put(MedReminderTableInfo.COLUMN_NAME_TIME, alarmTime);
			database.insert(MedReminderTableInfo.TABLE_NAME, null, cv2);
		}
		return (int) database.insert(MedTableInfo.TABLE_NAME, null, cv);
	}

	public Cursor getAllRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo._ID,
		  MedTableInfo.COLUMN_NAME_JSON_STRING,
		  MedTableInfo.COLUMN_NAME_REMINDER_SET
		};

		String sortOrder =
		  MedTableInfo._ID + " ASC";

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, null, null, null, null, sortOrder
		);
		return cursor;
	}

	public Cursor getAllRemindersRows(Context context){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedReminderTableInfo._ID,
		  MedReminderTableInfo.COLUMN_NAME_MEDICATION,
		  MedReminderTableInfo.COLUMN_NAME_TIME
		};

		String sortOrder =
		  MedReminderTableInfo._ID + " ASC";

		Cursor cursor = db.query(
		  MedReminderTableInfo.TABLE_NAME, projection, null, null, null, null, sortOrder
		);

		return cursor;
	}

	public MyMedication getMedicationStatement(Context context, int id){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getReadableDatabase();

		String[] projection = {
		  MedTableInfo.COLUMN_NAME_JSON_STRING, MedTableInfo.COLUMN_NAME_REMINDER_SET
		};

		Cursor cursor = db.query(
		  MedTableInfo.TABLE_NAME, projection, MedTableInfo._ID+" = "+id, null, null, null, null
		);

		if (cursor.moveToFirst()) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_JSON_STRING));
			MedicationStatement medStatement = (MedicationStatement) mFhirServices.toResource(jsonMedString);

			Boolean reminderSet = cursor.getInt(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_REMINDER_SET)) != 0;

			MyMedication myMedication = new MyMedication();
			myMedication.setFhirMedStatement(medStatement);
			myMedication.setReminderSet(reminderSet);
			return myMedication;
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

	public void updateMedication(Context context, int id, MyMedication updatedMyMedication){
		DatabaseInitializer dbo = DatabaseInitializer.getInstance(context);
		SQLiteDatabase db = dbo.getWritableDatabase();
		ContentValues cv = new ContentValues();
		MedicationStatement updatedMedStatement = updatedMyMedication.getFhirMedStatement();
		String updatedJsonMedString = mFhirServices.toJsonString(updatedMedStatement);
		cv.put(MedTableInfo.COLUMN_NAME_JSON_STRING, updatedJsonMedString);
		cv.put(MedTableInfo.COLUMN_NAME_REMINDER_SET, updatedMyMedication.getReminderSet());
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
