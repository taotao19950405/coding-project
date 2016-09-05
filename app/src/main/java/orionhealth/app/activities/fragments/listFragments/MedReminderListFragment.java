package orionhealth.app.activities.fragments.listFragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import orionhealth.app.R;
import orionhealth.app.activities.adaptors.ConditionListAdapter;
import orionhealth.app.activities.adaptors.MedReminderExpandableListAdaptor;
import orionhealth.app.activities.main.AddMedicationActivity;
import orionhealth.app.data.medicationDatabase.CondTableOperations;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 5/09/16.
 */
public class MedReminderListFragment extends ListFragment {

	public MedReminderListFragment() {
	}

	public static MedReminderListFragment newInstance() {
		MedReminderListFragment fragment = new MedReminderListFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_med_reminder_list, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Cursor cursor = MedTableOperations.getInstance().getAllRemindersRows((getContext()));

		ListView listView = getListView();
		MedReminderExpandableListAdaptor listAdapter = new MedReminderExpandableListAdaptor(getContext(), cursor);
		listView.setAdapter(listAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

	};
}
