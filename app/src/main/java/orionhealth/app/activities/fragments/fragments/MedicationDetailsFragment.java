package orionhealth.app.activities.fragments.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.widget.Toast;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.dialogFragments.RemoveMedicationDialogFragment;
import orionhealth.app.activities.main.MyMedicationActivity;
import orionhealth.app.data.dataModels.Unit;
import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.fhir.FhirServices;
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

	private DateService dateService;

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

		dateService = new DateService();

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

	public void addMedicationToDatabase(Context context) {
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
			medicationStatement = createMedStatement(name, dosage, unit, reasonForUse, startDate, endDate, instructions);
			MedTableOperations.getInstance().addToMedTable(context, medicationStatement);
			FhirServices.getsFhirServices().sendToServer(medicationStatement, context);
			Intent intent = new Intent(context, MyMedicationActivity.class);
			startActivity(intent);
		} catch (NoNameException e) {
			Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
		} catch (NoDosageException e) {
			Toast.makeText(context, "Please enter a dosage", Toast.LENGTH_SHORT).show();
		} catch (NumberFormatException e) {
			Toast.makeText(context, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void updateMedicationInDatabase(Context context){
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
			Intent intent = new Intent(context, MyMedicationActivity.class);
			startActivity(intent);
		} catch (NoNameException e){
			Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
		} catch (NoDosageException e){
			Toast.makeText(context, "Please enter a dosage", Toast.LENGTH_SHORT).show();
		} catch (NumberFormatException e){
			Toast.makeText(context, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private MedicationStatement createMedStatement(String name, String dosage, Unit unit,
												   String reasonForUse, String startDate,
												   String endDate, String note) throws Exception {
		checkValidMedication(name, dosage);
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
		Intent intent = new Intent(context, MyMedicationActivity.class);
		startActivity(intent);
	}

	public void onSetStartDate(int year, int monthOfYear, int dayOfMonth){
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		Date d = c.getTime();
		String dateString = dateService.formatToString(d);
		mStartDateTextField.setText(dateString);
	}

	public void onSetEndDate(int year, int monthOfYear, int dayOfMonth){
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		Date d = c.getTime();
		String dateString = dateService.formatToString(d);
		mEndDateTextFeild.setText(dateString);
	}

	public void onCancelStartDate() {
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
		mStartDateTextField.setOnFocusChangeListener(new showDatePickerFocusChangeListener("start"));
		mStartDateTextField.setOnClickListener(new showDatePickerClickListener("start"));
		mStartDateTextField.setShowSoftInputOnFocus(false);
		mStartDateTextField.setOnTouchListener(new hideKeyBoardTouchListener());

		mEndDateTextFeild.setOnFocusChangeListener(new showDatePickerFocusChangeListener("end"));
		mEndDateTextFeild.setOnClickListener(new showDatePickerClickListener("end"));
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
		private String tag;

		public showDatePickerFocusChangeListener(String tag) {
			this.tag = tag;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				DialogFragment dialogFragment = new DatePicker();
				dialogFragment.show(getFragmentManager(), tag);
				hideKeyBoard(v);
			}
		}
	}

	private class showDatePickerClickListener implements View.OnClickListener {
		private String tag;

		public showDatePickerClickListener(String tag) {
			this.tag = tag;
		}

		@Override
		public void onClick(View v) {
			DialogFragment dialogFragment = new DatePicker();
			dialogFragment.show(getFragmentManager(), tag);
		}
	}

// --------------------------   Exception classes ---------------------------------------------- //

	private class NoNameException extends Exception {

	}

	private class NoDosageException extends Exception {

	}

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePicker.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePicker and return it
//            return new DatePicker(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//           EditText dateEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_effectiveStart);
//            dateEditTextField.setText(day + "/" + month + "/" + year);
//        }
//
//
//        public void showDatePickerDialog(View v) {
//            DialogFragment newFragment = new DatePickerFragment();
//            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//        }
//
//    }


}


