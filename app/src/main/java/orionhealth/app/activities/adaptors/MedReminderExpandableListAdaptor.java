package orionhealth.app.activities.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.services.DateService;

import java.util.Calendar;

/**
 * Created by bill on 5/09/16.
 */
public class MedReminderExpandableListAdaptor extends CursorAdapter {

	public MedReminderExpandableListAdaptor(Context context, Cursor c) {
		super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater =
		  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_med_reminder_list_item, null);
		return result;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView reminderText = (TextView) view.findViewById(R.id.list_display_reminder);
		TextView dateText = (TextView) view.findViewById(R.id.list_display_date);

		String text =
		  	cursor.getString(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING));
		Long l =
			cursor.getLong(cursor.getColumnIndex(DatabaseContract.MedReminderTableInfo.COLUMN_NAME_TIME));

		MedicationStatement medicationStatement =
		  	(MedicationStatement) FhirServices.getsFhirServices().toResource(text);
		CodeableConceptDt conceptDt = (CodeableConceptDt) medicationStatement.getMedication();
		reminderText.setText(conceptDt.getText());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(l);
		DateService dateService = new DateService();
		dateService.setFormat(DateService.FLAG_TIME_FORMAT);
		dateText.setText(dateService.formatToString(calendar.getTime()));
	}
}
