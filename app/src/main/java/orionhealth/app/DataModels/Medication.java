package orionhealth.app.DataModels;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import orionhealth.app.MedicationDatabase.DatabaseContract;
import orionhealth.app.MedicationDatabase.DatabaseOperations;

/**
 * Created by bill on 8/04/16.
 */
public class Medication {
	private String name;
	private int dosage;

	public Medication(String name, int dosage){
		this.name = name;
		this.dosage = dosage;
	}

	public void addToMedTable(DatabaseOperations dob){
		SQLiteDatabase database = dob.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DatabaseContract.MedTableInfo.COLUMN_NAME_NAME, name);
		cv.put(DatabaseContract.MedTableInfo.COLUMN_NAME_DOSAGE, dosage);
		database.insert(DatabaseContract.MedTableInfo.TABLE_NAME, null, cv);
	}
}
