package orionhealth.app.activities.fragments.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.valueset.ConditionCategoryCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.dialogFragments.RemoveConditionDialogFragment;
import orionhealth.app.activities.main.MainActivity;
import orionhealth.app.activities.main.MainActivity;
import orionhealth.app.data.dataModels.Category;
import orionhealth.app.data.dataModels.Severity;
import orionhealth.app.data.dataModels.VerificationStatus;
import orionhealth.app.data.medicationDatabase.CondTableOperations;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.services.DateService;

/**
 * Created by Lu on 16/07/16.
 */

//Issue: date pasring issue unsolved
public class ConditionDetailsFragment extends Fragment {

    private VerificationStatus[] verificationStatuses;
    private Category[] categories;
    private Severity[] severities;

    private int mConditionID;
    private Condition mCondition;

    private EditText mConditionTextField;
    private EditText mRecordedDateTextField;
    private EditText mEvidenceTextField;
    private EditText mNotesTextField;

    private Spinner mCategorySelector;
    private Spinner mSeveritySelector;
    private Spinner mVerificationStatusSelector;

    private DateService dateService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsFragment = inflater.inflate(R.layout.fragment_condition_details, container, false);

        mConditionTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_condition);
        mEvidenceTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_evidence);
        mNotesTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_notes);

        mRecordedDateTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_date);
        setUpDateEditTextFields();

        categories = Category.values();
        mCategorySelector = (Spinner) detailsFragment.findViewById(R.id.category_spinner);
        setUpSelector(categories, mCategorySelector);

        severities = Severity.values();
        mSeveritySelector = (Spinner) detailsFragment.findViewById(R.id.severity_spinner);
        setUpSelector(severities, mSeveritySelector);

        verificationStatuses = VerificationStatus.values();
        mVerificationStatusSelector = (Spinner) detailsFragment.findViewById(R.id.verification_spinner);
        setUpSelector(verificationStatuses, mVerificationStatusSelector);

        mNotesTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_notes);

        dateService = new DateService();

        return detailsFragment;
    }

    public void populateFields() {
        if (mCondition != null) {
            EditText conditionEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_condition);
            CodeableConceptDt conditionCodeableConcept = mCondition.getCode();
            conditionEditTextField.setText(conditionCodeableConcept.getText());

            EditText evidenceEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_evidence);
            List<Condition.Evidence> listEvidence = mCondition.getEvidence();
            if (!listEvidence.isEmpty()) {
                Condition.Evidence evidence = listEvidence.get(0);
                CodeableConceptDt evidenceCodableConcept = evidence.getCode();
                evidenceEditTextField.setText(evidenceCodableConcept.getText());
            }

            EditText notesEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_notes);
            String notesString = mCondition.getNotes();
            notesEditTextField.setText(notesString);

            CodeableConceptDt categoryCodeableConcept = mCondition.getCategory();
            Spinner spinner = (Spinner) getActivity().findViewById(R.id.category_spinner);
            if (!categoryCodeableConcept.isEmpty()) {
                String myString = categoryCodeableConcept.getText();
                int index = 0;
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                        index = i;
                        break;
                    }
                }
                spinner.setSelection(index);
            }

            CodeableConceptDt severityCodeableConcept = mCondition.getSeverity();
            spinner = (Spinner) getActivity().findViewById(R.id.severity_spinner);
            if (!severityCodeableConcept.isEmpty()) {
                String myString = severityCodeableConcept.getText();
                int index = 0;
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                        index = i;
                        break;
                    }
                }
                spinner.setSelection(index);
            }

            String verificationStatus = mCondition.getVerificationStatus();
            spinner = (Spinner) getActivity().findViewById(R.id.verification_spinner);
            if (verificationStatus.length() > 0) {
                int index = 0;
                for (int i = 0; i < spinner.getCount(); i++) {
                    if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(verificationStatus)) {
                        index = i;
                        break;
                    }
                }
                spinner.setSelection(index);
            }


            DateDt p = mCondition.getDateRecordedElement();

            if (p != null) {
                String dateString = dateService.formatToString(p.getValue());
                mRecordedDateTextField.setText(dateString);
            }
            System.out.println("Hello");
        }

    }

    public void addConditionToDatabase(Context context) throws Exception {
        //Do something in response to clicking add button
        String conditionCode = mConditionTextField.getText().toString();
        String recordedDate = mRecordedDateTextField.getText().toString();
        String evidence = mEvidenceTextField.getText().toString();
        String notes = mNotesTextField.getText().toString();

        Category category = (Category) mCategorySelector.getSelectedItem();
        Severity severity = (Severity) mSeveritySelector.getSelectedItem();
        VerificationStatus verificationStatus = (VerificationStatus) mVerificationStatusSelector.getSelectedItem();

        Condition condition;
        try {
            condition = createCondition(conditionCode, recordedDate, evidence, notes,
                    category.toString(), severity.toString(), verificationStatus.toString());
            CondTableOperations.getInstance().addToCondTable(context, condition);
            FhirServices.getsFhirServices().sendToServer(condition, context);
        } catch (NoNameException e) {
            Toast.makeText(context, "Please enter a condition", Toast.LENGTH_SHORT).show();
			throw e;
        } catch (NoVerificationStatusException e) {
            Toast.makeText(context, "Please select a verification status", Toast.LENGTH_SHORT).show();
			throw e;
        } catch (Exception e) {
            e.printStackTrace();
			throw e;
        }
    }

    public void updateConditionInDatabase(Context context) throws Exception {
        String conditionCode = mConditionTextField.getText().toString();
        String recordedDate = mRecordedDateTextField.getText().toString();
        String evidence = mEvidenceTextField.getText().toString();
        String notes = mNotesTextField.getText().toString();

        Category category = (Category) mCategorySelector.getSelectedItem();
        Severity severity = (Severity) mSeveritySelector.getSelectedItem();
        VerificationStatus verificationStatus = (VerificationStatus) mVerificationStatusSelector.getSelectedItem();
        try {
            mCondition =
                    createCondition(conditionCode, recordedDate, evidence, notes,
                            category.toString(), severity.toString(), verificationStatus.toString());
            CondTableOperations.getInstance().updateCondition(context, mConditionID, mCondition);
        } catch (NoNameException e) {
            Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show();
			throw e;
        } catch (NoVerificationStatusException e) {
            Toast.makeText(context, "Please select a verification status", Toast.LENGTH_SHORT).show();
        	throw e;
		} catch (NoDateRecordedException e) {
            Toast.makeText(context, "Please select a verification status", Toast.LENGTH_SHORT).show();
    		throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }

    private Condition createCondition(String conditionCode, String recordedDate,
                                      String evidence, String notes,
                                      String category, String severity, String verificationStatus) throws Exception {

        checkValidCondition(conditionCode, verificationStatus, recordedDate);
        Condition condition = new Condition();
        condition.setCode(new CodeableConceptDt().setText(conditionCode));
        ResourceReferenceDt patientRef = new ResourceReferenceDt().setDisplay("LOCAL");
        condition.setPatient(patientRef);

        condition.setNotes(notes);

        Date d = dateService.parseDate(recordedDate);
        DateDt date = new DateDt();
        date.setPrecision(TemporalPrecisionEnum.DAY);
        date.setValue(d);
        condition.setDateRecorded(date);

//      Enum fixed
        condition.setCategory(ConditionCategoryCodesEnum.forCode(category.toLowerCase()));
        condition.setSeverity(new CodeableConceptDt().setText(severity));
        condition.setVerificationStatus(ConditionVerificationStatusEnum.forCode(verificationStatus.toLowerCase()));

        Condition.Evidence evidenceFhir = new Condition.Evidence();
        evidenceFhir.setCode(new CodeableConceptDt().setText(evidence));
        List<Condition.Evidence> listEvidence = new LinkedList<Condition.Evidence>();
        listEvidence.add(evidenceFhir);
        condition.setEvidence(listEvidence);
        return condition;
    }

    public void removeCondition() {
        DialogFragment removeCondDialogue = new RemoveConditionDialogFragment();
        removeCondDialogue.show(getFragmentManager(), "removeCondition");
    }

    public void onRemovePositiveClick(Context context) {
        CondTableOperations.getInstance().removeCondition(context, mConditionID);
    }

    public void onSetDate(int year, int monthOfYear, int dayOfMonth, String tag) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        Date d = c.getTime();
        String dateString = dateService.formatToString(d);
        if (tag.equals(mRecordedDateTextField.getId() + "")) {
            mRecordedDateTextField.setText(dateString);
        }
    }

    public void onCancelDate() {
        mNotesTextField.requestFocus();
    }

    public void setCondition(Context context, int condLocalId) {
        mConditionID = condLocalId;
        mCondition = CondTableOperations.getInstance().getCondition(context, condLocalId);
    }

    private void checkValidCondition(String name, String verificationStatus, String recordedDate) throws Exception {
        if (name.equals("")) {
            throw new NoNameException();
        } else if (verificationStatus.equals("")) {
            throw new NoVerificationStatusException();
        } else if (recordedDate.equals("")) {
            throw new NoVerificationStatusException();
        }
    }

    private void setUpDateEditTextFields() {
        mRecordedDateTextField.setOnFocusChangeListener(new showDatePickerFocusChangeListener(mRecordedDateTextField.getId() + ""));
        mRecordedDateTextField.setOnClickListener(new showDatePickerClickListener(mRecordedDateTextField.getId() + ""));
        mRecordedDateTextField.setShowSoftInputOnFocus(false);
        mRecordedDateTextField.setOnTouchListener(new hideKeyBoardTouchListener());
    }

    private void setUpSelector(Object[] cs, Spinner selector) {
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, cs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selector.setAdapter(adapter);
    }

    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    private class NoVerificationStatusException extends Exception {

    }

    private class NoDateRecordedException extends Exception {

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

