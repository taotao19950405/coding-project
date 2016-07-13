package orionhealth.app.activities.fragments.listFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import orionhealth.app.R;
import orionhealth.app.activities.main.AddSymptomsActivity;
import orionhealth.app.activities.main.EditSymptomsActivity;

/**
 * Created by Lu on 13/07/16.
 */
public class SymptomsListFragment extends ListFragment {


	private String[] values = new String[] { "Android List View",
			"Adapter implementation",
			"Simple List View In Android",
			"Create List View Android",
	};
	private int mToViews = R.id.hardcoded_display_symptoms;

	public SymptomsListFragment() {
	}

	public static SymptomsListFragment newInstance() {
		SymptomsListFragment fragment = new SymptomsListFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_symptoms_list, container, false);
		FloatingActionButton addButton =  (FloatingActionButton) view.findViewById(R.id.button_add_symptoms);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), AddSymptomsActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(getContext(), R.layout.fragment_symptoms_list_items, mToViews, values);
		setListAdapter(adapter);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getContext(), EditSymptomsActivity.class);
//		intent.putExtra(SELECTED_MED_ID, id);
		startActivity(intent);
	};
}
