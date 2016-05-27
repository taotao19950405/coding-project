package orionhealth.app.activities.fragments.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationDetailsFragment extends Fragment {
    Calendar calendar;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medication_details, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.unit_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return rootView;
    }


    public void populateFields(MedicationStatement medicationStatement) {
        EditText nameEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_name);
		CodeableConceptDt codeableConcept = (CodeableConceptDt) medicationStatement.getMedication();
        nameEditTextField.setText(codeableConcept.getText());

        EditText dosageEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_dosage);
		List<MedicationStatement.Dosage> listDosage = medicationStatement.getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);
		SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
		dosageEditTextField.setText(simpleQuantityDt.getValueElement().getValueAsInteger()+"");

		String myString = simpleQuantityDt.getUnit();
		Spinner spinner = (Spinner) getActivity().findViewById(R.id.unit_spinner);
		int index = 0;

		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
				index = i;
				break;
			}
		}

		spinner.setSelection(index);


		EditText reasonForUseEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_reasonForUse);
		codeableConcept = (CodeableConceptDt) medicationStatement.getReasonForUse();

		if (codeableConcept != null) {
			reasonForUseEditTextField.setText(codeableConcept.getText());
		}

		EditText instructionsEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_instructions);
		instructionsEditTextField.setText(medicationStatement.getNote());

    }

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
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


