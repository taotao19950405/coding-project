package orionhealth.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import orionhealth.app.R;
import orionhealth.app.dataModels.Medication;

/**
 * Created by bill on 23/04/16.
 */
public final class Fragments {

	public static class UnderConstructionFragment extends Fragment {

		public static UnderConstructionFragment newInstance() {
			UnderConstructionFragment fragment = new UnderConstructionFragment();
			return fragment;
		}

		public UnderConstructionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_construction, container, false);
			return rootView;
		}
	}

	public static class MedicationDetailsFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_medication_details, container, false);
			return rootView;
		}

		public void populateFields(Medication medication){
			EditText nameEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_name);
			nameEditTextField.setText(medication.getName());
			EditText dosageEditTextField = (EditText) getActivity().findViewById(R.id.edit_text_dosage);
			dosageEditTextField.setText(""+ medication.getDosage());
		}
	}

}
