package orionhealth.app.activities.fragments.listFragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import orionhealth.app.R;
import orionhealth.app.activities.main.AddMedicationActivity;
import orionhealth.app.activities.main.EditMedicationActivity;
import orionhealth.app.activities.external.AnimatedExpandableListView;
import orionhealth.app.activities.adaptors.MyExpandableListAdapter;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 25/04/16.
 */
public class MedicationListFragment extends ListFragment {

	public final static String SELECTED_MED_ID = "medicationListFragment.SELECTED_MED_ID";
	private AnimatedExpandableListView mAnimatedExpandableListView;

	public MedicationListFragment() {
	}

	public static MedicationListFragment newInstance() {
		MedicationListFragment fragment = new MedicationListFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_medication_list, container, false);
		FloatingActionButton addButton =  (FloatingActionButton) view.findViewById(R.id.button_add);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), AddMedicationActivity.class);
				startActivity(intent);
			}
		});
		return view;
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Cursor cursor = MedTableOperations.getInstance().getAllRows(getContext());
		mAnimatedExpandableListView = (AnimatedExpandableListView) getListView();
		mAnimatedExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				if (mAnimatedExpandableListView.isGroupExpanded(groupPosition)) {
					mAnimatedExpandableListView.collapseGroupWithAnimation(groupPosition);
				} else {
					mAnimatedExpandableListView.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}
		});
		MyExpandableListAdapter listAdapter = new MyExpandableListAdapter(getContext(), cursor){
			@Override
			public void OnEditButtonClick(int medicationLocalId){
				Intent intent = new Intent(getContext(), EditMedicationActivity.class);
				intent.putExtra(SELECTED_MED_ID, medicationLocalId);
				startActivity(intent);
			}
		};
		mAnimatedExpandableListView.setAdapter(listAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

	};
}
