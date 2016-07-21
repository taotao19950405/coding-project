//		 @author:  Bill
package orionhealth.app.data.medicationDatabase;

import android.provider.BaseColumns;

/**
 * Created by bill on 8/04/16.
 */
public final class DatabaseContract {

    public DatabaseContract() {
    }

	public static  abstract class MedTableInfo implements BaseColumns {
		public static final String TABLE_NAME = "medication";
		public static final String COLUMN_NAME_JSON_STRING = "json_string";
	}

	public static abstract class AllergyTableInfo implements BaseColumns{
		public static final String TABLE_NAME = "allergy";
		public static final String COLUMN_NAME_JSON_STRING = "json_string_allergy";
	}
}
