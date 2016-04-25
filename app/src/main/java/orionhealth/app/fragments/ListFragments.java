package orionhealth.app.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import orionhealth.app.R;
import orionhealth.app.activities.EditMedicationActivity;
import orionhealth.app.medicationDatabase.DatabaseContract.MedTableInfo;
import orionhealth.app.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 20/04/16.
 */
public final class ListFragments {

	public static class MedicationListFragment extends ListFragment {

		public final static String SELECTED_MED_ID = "orionhealth.app.fragments.listfragments.SELECTED_MED_ID";
		private String[] mFromColumns = {MedTableInfo.COLUMN_NAME_NAME, MedTableInfo.COLUMN_NAME_DOSAGE};
		private int[] mToViews = {R.id.list_display_name, R.id.list_display_dosage};

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.fragment_medication_list, container, false);
			return view;
		};

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			Cursor cursor = MedTableOperations.getAllRows(getContext());
			SimpleCursorAdapter adapter =
			  new SimpleCursorAdapter(getContext(), R.layout.fragment_medication_list_item, cursor, mFromColumns, mToViews, 0);
			setListAdapter(adapter);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id){
			super.onListItemClick(l, v, position, id);
			Intent intent = new Intent(getContext(), EditMedicationActivity.class);
			intent.putExtra(SELECTED_MED_ID, id);
			startActivity(intent);
		};

	}
}
