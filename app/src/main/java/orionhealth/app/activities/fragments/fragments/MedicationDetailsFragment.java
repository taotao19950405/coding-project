package orionhealth.app.activities.fragments.fragments;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;

import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.dialogFragments.RemoveMedicationDialogFragment;
import orionhealth.app.activities.main.MainActivity;
import orionhealth.app.data.dataModels.NotificationParcel;
import orionhealth.app.data.dataModels.Unit;
import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.services.AlarmReceiver;
import orionhealth.app.services.DateService;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationDetailsFragment extends Fragment {
	private Unit[] units;

	private int mMedicationID;
	private MedicationStatement mMedication;

	private EditText mNameTextField;
	private EditText mDosageTextField;
	private Spinner mDosageUnitSelector;
	private EditText mReasonTextField;
	private EditText mStartDateTextField;
	private EditText mEndDateTextFeild;
	private EditText mNotesTextField;
	private TimePicker mTimePicker;

	private DateService dateService;
	private AlarmManager alarmManager;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsFragment = inflater.inflate(R.layout.fragment_medication_details, container, false);

		mNameTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_name);
		mDosageTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_dosage);

		units = Unit.values();
        mDosageUnitSelector = (Spinner) detailsFragment.findViewById(R.id.unit_spinner);
		setUpUnitSelector();

		mReasonTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_reasonForUse);
		mStartDateTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_effectiveStart);
		mEndDateTextFeild = (EditText) detailsFragment.findViewById(R.id.edit_text_effectiveEnd);
		setUpDateEditTextFields();

		mNotesTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_notes);
		mTimePicker = (TimePicker) detailsFragment.findViewById(R.id.time_picker);

		dateService = new DateService();
		alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        return detailsFragment;
    }


    public void populateFields() {
		if (mMedication != null) {
			EditText nameEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_name);
			CodeableConceptDt codeableConcept = (CodeableConceptDt) mMedication.getMedication();
			nameEditTextField.setText(codeableConcept.getText());

			EditText dosageEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_dosage);
			List<MedicationStatement.Dosage> listDosage = mMedication.getDosage();
			MedicationStatement.Dosage dosage = listDosage.get(0);
			SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
			dosageEditTextField.setText(simpleQuantityDt.getValueElement().getValueAsInteger() + "");

			String unitIdString = simpleQuantityDt.getCode();
			Spinner spinner = (Spinner) getActivity().findViewById(R.id.unit_spinner);
			if (unitIdString == null) {
				String myString = simpleQuantityDt.getUnit();
				int index = 0;
				for (int i = 0; i < spinner.getCount(); i++) {
					if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
						index = i;
						break;
					}
				}
				spinner.setSelection(index);
			} else {
				int unitId = Integer.parseInt(unitIdString);
				spinner.setSelection(unitId);
			}
			EditText reasonForUseEditTextField =
			  (EditText) getActivity().findViewById(R.id.edit_text_reasonForUse);
			codeableConcept = (CodeableConceptDt) mMedication.getReasonForUse();

			if (codeableConcept != null) {
				reasonForUseEditTextField.setText(codeableConcept.getText());
			}

			PeriodDt p = (PeriodDt) mMedication.getEffective();

			if (p != null) {
				Date d = p.getStart();
				String dateString = dateService.formatToString(d);
				mStartDateTextField.setText(dateString);

				d = p.getEnd();
				dateString = dateService.formatToString(d);
				mEndDateTextFeild.setText(dateString);
			}

			EditText instructionsEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_notes);
			instructionsEditTextField.setText(mMedication.getNote());
		}

    }

	public void addMedicationToDatabase(Context context) throws Exception {
		//Do something in response to clicking add button
		String name = mNameTextField.getText().toString();
		String dosage = mDosageTextField.getText().toString();
		Unit unit = (Unit) mDosageUnitSelector.getSelectedItem();
		String reasonForUse = mReasonTextField.getText().toString();
		String startDate = mStartDateTextField.getText().toString();
		String endDate = mEndDateTextFeild.getText().toString();
		String instructions = mNotesTextField.getText().toString();

		MedicationStatement medicationStatement;
		try {
			mMedication = createMedStatement(name, dosage, unit, reasonForUse, startDate, endDate, instructions);
			mMedicationID = MedTableOperations.getInstance().addToMedTable(context, mMedication);
			FhirServices.getsFhirServices().sendToServer(mMedication, context);
			Intent alarmIntent = new Intent(context, AlarmReceiver.class);
			NotificationParcel parcel =
			  		new NotificationParcel(mMedicationID, Character.toUpperCase(name.charAt(0)) + name.substring(1), instructions, unit.ordinal());
			Bundle bundle = new Bundle();
			bundle.putParcelable(AlarmReceiver.PARCEL_KEY, parcel);
			alarmIntent.putExtra(AlarmReceiver.MEDICATION_KEY, bundle);
			PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, mMedicationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			Calendar calendar = Calendar.getInstance();
			if (Build.VERSION.SDK_INT >= 23 ) {
				calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
				  mTimePicker.getHour(), mTimePicker.getMinute(), 1);
			} else {
				calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
				  mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute(), 2);
			}
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
			  alarmPendingIntent);
		} catch (NoNameException e) {
			Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (NoDosageException e) {
			Toast.makeText(context, "Please enter a dosage", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (NumberFormatException e) {
			Toast.makeText(context, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	public void updateMedicationInDatabase(Context context) throws Exception{
		String name = mNameTextField.getText().toString();
		String dosage = mDosageTextField.getText().toString();
		Unit unit = (Unit) mDosageUnitSelector.getSelectedItem();
		String reasonForUse = mReasonTextField.getText().toString();
		String startDate = mStartDateTextField.getText().toString();
		String endDate = mEndDateTextFeild.getText().toString();
		String notes = mNotesTextField.getText().toString();
		try {
			mMedication =
			  		createMedStatement(name, dosage, unit, reasonForUse, startDate, endDate, notes);
			MedTableOperations.getInstance().updateMedication(context, mMedicationID, mMedication);
		} catch (NoNameException e){
			Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (NoDosageException e){
			Toast.makeText(context, "Please enter a dosage", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (NumberFormatException e){
			Toast.makeText(context, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
			throw e;
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	private MedicationStatement createMedStatement(String name, String dosage, Unit unit,
												   String reasonForUse, String startDate,
												   String endDate, String note) throws Exception {
		checkValidMedication(name, dosage);
		name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		Long dosageLong = Long.parseLong(dosage);
		MedicationStatement medicationStatement = new MedicationStatement();
		medicationStatement.setMedication(new CodeableConceptDt().setText(name));
		medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);
		ResourceReferenceDt patientRef = new ResourceReferenceDt().setDisplay("LOCAL");
		medicationStatement.setPatient(patientRef);
		medicationStatement.setReasonForUse(new CodeableConceptDt().setText(reasonForUse));
		Date d = dateService.parseDate(startDate);
		PeriodDt period = new PeriodDt();
		period.setStart(d, TemporalPrecisionEnum.DAY );
		d = dateService.parseDate(endDate);
		period.setEnd(d, TemporalPrecisionEnum.DAY);
		medicationStatement.setEffective(period);

		medicationStatement.setNote(note);
		MedicationStatement.Dosage dosageFhir = new MedicationStatement.Dosage();
		SimpleQuantityDt simpleQuantityDt = new SimpleQuantityDt(dosageLong);
		simpleQuantityDt.setUnit(unit.toString());
		simpleQuantityDt.setCode(unit.ordinal()+"");
		dosageFhir.setQuantity(simpleQuantityDt);
		List<MedicationStatement.Dosage> listDosage = new LinkedList<MedicationStatement.Dosage>();
		listDosage.add(dosageFhir);
		medicationStatement.setDosage(listDosage);
		return medicationStatement;
	}

	public void removeMedication(){
		DialogFragment removeMedDialogue = new RemoveMedicationDialogFragment();
		removeMedDialogue.show(getFragmentManager(), "removeMedication");
	}

	public void onRemovePositiveClick(Context context) {
		MedTableOperations.getInstance().removeMedication(context, mMedicationID);
		Intent alarmIntent = new Intent(context, AlarmReceiver.class);
		PendingIntent alarmPendingIntent
		  		= PendingIntent.getBroadcast(context, mMedicationID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(alarmPendingIntent);
	}

	public void onSetDate(int year, int monthOfYear, int dayOfMonth, String tag){
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		Date d = c.getTime();
		String dateString = dateService.formatToString(d);
		if (tag.equals(mStartDateTextField.getId()+"")) {
			mStartDateTextField.setText(dateString);
		} else {
			mEndDateTextFeild.setText(dateString);
		}
	}

	public void onCancelDate() {
		mNotesTextField.requestFocus();
	}

	public void setMedication(Context context, int medLocalId){
		mMedicationID = medLocalId;
		mMedication = MedTableOperations.getInstance().getMedicationStatement(context, medLocalId);
	}

	private void checkValidMedication(String name, String dosage) throws Exception {
		if (name.equals("")){
			throw new NoNameException();
		} else if (dosage.equals("")){
			throw  new NoDosageException();
		}
		Long.parseLong(dosage);
	}

	private void setUpDateEditTextFields() {
		mStartDateTextField.setOnFocusChangeListener(new showDatePickerFocusChangeListener());
		mStartDateTextField.setOnClickListener(new showDatePickerClickListener());
		mStartDateTextField.setShowSoftInputOnFocus(false);
		mStartDateTextField.setOnTouchListener(new hideKeyBoardTouchListener());

		mEndDateTextFeild.setOnFocusChangeListener(new showDatePickerFocusChangeListener());
		mEndDateTextFeild.setOnClickListener(new showDatePickerClickListener());
		mEndDateTextFeild.setShowSoftInputOnFocus(false);
		mEndDateTextFeild.setOnTouchListener(new hideKeyBoardTouchListener());
	}

	private void setUpUnitSelector() {
		ArrayAdapter<CharSequence> adapter =
		  new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, units);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mDosageUnitSelector.setAdapter(adapter);
	}


	private void hideKeyBoard(View view) {
		InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  // hide the soft keyboard
		}
	}

// ------------------------------	Listener classes   --------------------------------------------------//

	private class hideKeyBoardTouchListener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			hideKeyBoard(v);
			return false;
		}
	}

	private class showDatePickerFocusChangeListener implements View.OnFocusChangeListener {

		public showDatePickerFocusChangeListener() {
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				DatePicker datePicker = new DatePicker();
				EditText editText = (EditText) v;
				Date date = dateService.parseDate(editText.getText().toString());
				if (date != null) {
					final Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					datePicker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				} else {
					datePicker.setDate();
				}
				datePicker.show(getFragmentManager(), v.getId()+"");
			}
		}
	}

	private class showDatePickerClickListener implements View.OnClickListener {

		public showDatePickerClickListener() {
		}

		@Override
		public void onClick(View v) {
			DatePicker datePicker = new DatePicker();
			EditText editText = (EditText) v;
			Date date = dateService.parseDate(editText.getText().toString());
			if (date != null) {
				final Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				datePicker.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			} else {
				datePicker.setDate();
			}
			datePicker.show(getFragmentManager(), v.getId()+"");
		}
	}

// --------------------------   Exception classes ---------------------------------------------- //

	private class NoNameException extends Exception {

	}

	private class NoDosageException extends Exception {

	}

}


