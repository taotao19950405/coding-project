package orionhealth.app.activities.fragments.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import orionhealth.app.R;

/**
 * Created by Lu on 13/07/16.
 */
public class SymptomsFragment extends Fragment {

	public SymptomsFragment() {
	}

	public static SymptomsFragment newInstance() {
		SymptomsFragment fragment = new SymptomsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_symptoms_list, container, false);
		return view;
	}
}
