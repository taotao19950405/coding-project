package orionhealth.app.layouts.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.layouts.externalResources.AnimatedExpandableListView;

/**
 * Created by bill on 4/05/16.
 */
public class MyExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
	private Context context;
	private Cursor cursor;

	public MyExpandableListAdapter(Context context) {
		this.context = context;
	}
	public MyExpandableListAdapter(Context context, Cursor cursor){
		this.context = context;
		this.cursor = cursor;
	}

	public void OnIndicatorClick(boolean isExpanded, int position){

	}

	public void OnTextClick(){

	}

	@Override
	public int getGroupCount() {
		return cursor.getCount();
	}


	@Override
	public Object getGroup(int groupPosition) {
		if (cursor.moveToPosition(groupPosition)) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING));
			FhirContext fhirContext = FhirServices.getFhirServices().getFhirContextInstance();
			MedicationStatement medStatement = (MedicationStatement) fhirContext.newJsonParser().parseResource(jsonMedString);
			return medStatement;
		}else {
			return null;
		}
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
		  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_medication_list_item, null);
		MedicationStatement medStatement = (MedicationStatement) getGroup(groupPosition);
		CodeableConceptDt codeableConcept = (CodeableConceptDt)medStatement.getMedication();
		String name = codeableConcept.getText();
		TextView textView = (TextView) result.findViewById(R.id.list_display_name);
		textView.setText(name);

		ImageView indicator = (ImageView) result.findViewById(R.id.indicator);
		indicator.setSelected(isExpanded);
		indicator.setTag(groupPosition);

		indicator.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer)v.getTag();
				OnIndicatorClick(isExpanded,position);

			}
		});
		return result;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
		  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_trial, null);
		return result;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
