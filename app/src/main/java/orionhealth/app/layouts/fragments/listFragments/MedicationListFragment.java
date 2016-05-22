package orionhealth.app.layouts.fragments.listFragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import orionhealth.app.R;
import orionhealth.app.activities.EditMedicationActivity;
import orionhealth.app.layouts.externalResources.AnimatedExpandableListView;
import orionhealth.app.layouts.adaptors.MyExpandableListAdapter;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationListFragment extends ListFragment {

	public final static String SELECTED_MED_ID = "orionhealth.app.layouts.fragments.listfragments.SELECTED_MED_ID";
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
//		animatedExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//				// We call collapseGroupWithAnimation(int) and
//				// expandGroupWithAnimation(int) to animate group
//				// expansion/collapse.
//				if (animatedExpandableListView.isGroupExpanded(groupPosition)) {
//					animatedExpandableListView.collapseGroupWithAnimation(groupPosition);
//				} else {
//					animatedExpandableListView.expandGroupWithAnimation(groupPosition);
//				}
//				return true;
//			}
//
//		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

	};
}
