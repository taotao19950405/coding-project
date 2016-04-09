package orionhealth.app.dataModels;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import orionhealth.app.medicationDatabase.DatabaseContract;
import orionhealth.app.medicationDatabase.DatabaseOperations;

/**
 * Created by bill on 8/04/16.
 */
public class Medication {
	private String name;
	private int dosage;

	public Medication(String name, int dosage) {
		this.name = name;
		this.dosage = dosage;
	}

	public String getName() {
		return name;
	}

	public int getDosage() {
		return dosage;
	}
}
