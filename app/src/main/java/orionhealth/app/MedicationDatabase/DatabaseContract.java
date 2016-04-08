package orionhealth.app.MedicationDatabase;

import android.provider.BaseColumns;

/**
 * Created by bill on 8/04/16.
 */
public final class DatabaseContract {

	public DatabaseContract() {}

	public static abstract class MedTableInfo implements BaseColumns {
		public static final String TABLE_NAME = "medication";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_DOSAGE = "dosage";
	}
}
