package orionhealth.app.layouts.fragments.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationDetailsFragment extends Fragment {

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
        dosageEditTextField.setText("");

    }

}
