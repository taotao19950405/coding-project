package orionhealth.app.activities.fragments.listFragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import orionhealth.app.R;
import orionhealth.app.activities.main.EditMedicationActivity;
import orionhealth.app.activities.externalResources.AnimatedExpandableListView;
import orionhealth.app.activities.adaptors.MyExpandableListAdapter;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationListFragment extends ListFragment {

	public final static String SELECTED_MED_ID = "medicationListFragment.SELECTED_MED_ID";
	private String[] mFromColumns = {DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING};
	private int[] mToViews = {R.id.list_display_name};
	private AnimatedExpandableListView animatedExpandableListView;

	public MedicationListFragment() {
	}

	public static MedicationListFragment newInstance() {
		MedicationListFragment fragment = new MedicationListFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_medication_list, container, false);
		return view;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Cursor cursor = MedTableOperations.getAllRows(getContext());
		animatedExpandableListView = (AnimatedExpandableListView) getListView();
		MyExpandableListAdapter listAdapter = new MyExpandableListAdapter(getContext(), cursor) {
			@Override
			public void OnIndicatorClick(boolean isExpanded, int position) {
				if (isExpanded) {
					animatedExpandableListView.collapseGroupWithAnimation(position);
				} else {
					animatedExpandableListView.expandGroupWithAnimation(position);
				}
			}

			@Override
			public void OnTextClick(int medLocalId){
				Intent intent = new Intent(getContext(), EditMedicationActivity.class);
				intent.putExtra(SELECTED_MED_ID, medLocalId);
				startActivity(intent);
			}

		};

		animatedExpandableListView.setAdapter(listAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

	};
}
