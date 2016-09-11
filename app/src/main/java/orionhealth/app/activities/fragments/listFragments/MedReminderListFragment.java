package orionhealth.app.activities.fragments.listFragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import orionhealth.app.R;
import orionhealth.app.activities.adaptors.MedReminderListAdaptor;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 5/09/16.
 */
public class MedReminderListFragment extends ListFragment {
	private View pendingMedTitle;

	private MedReminderListAdaptor cursorAdapter;

	public MedReminderListFragment() {
	}

	public static MedReminderListFragment newInstance() {
		MedReminderListFragment fragment = new MedReminderListFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_med_reminder_list, container, false);
		pendingMedTitle = inflater.inflate(R.layout.pending_med_title, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Cursor cursor = MedTableOperations.getInstance().getAllRemindersRows((getContext()));

		ListView listView = getListView();
		MedReminderListAdaptor listAdapter = new MedReminderListAdaptor(getContext(), cursor);
		cursorAdapter = listAdapter;
		listView.setAdapter(listAdapter);
		listView.addHeaderView(pendingMedTitle);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

	};

	public MedReminderListAdaptor getCursorAdapter() {
		return cursorAdapter;
	}
}
