package orionhealth.app.activities.fragments.dialogFragments;

import android.app.*;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by bill on 22/06/16.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	public interface DatePickerListener {
		void onSetStartDate(int year, int monthOfYear, int dayOfMonth);
		void onCancelStartDate();
	}

	DatePickerListener datePickerListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the RemoveMedDialogListener so we can send events to the host
			datePickerListener = (DatePickerListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
			  + " must implement datePickerListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePicker and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		datePickerListener.onSetStartDate(year, monthOfYear, dayOfMonth);
	}

	public void onCancel(DatePickerDialog dialog){
		super.onCancel(dialog);
		datePickerListener.onCancelStartDate();
	}
}
