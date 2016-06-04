package orionhealth.app.activities.adaptors;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.activities.main.MyMedicationActivity;
import orionhealth.app.data.dataModels.MyMedicationStatement;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.activities.externalResources.AnimatedExpandableListView;

import java.util.List;

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

	public void OnEditButtonClick(int position){

	}


	@Override
	public int getGroupCount() {
		return cursor.getCount();
	}


	@Override
	public Object getGroup(int groupPosition) {
		if (cursor.moveToPosition(groupPosition)) {
			String jsonMedString = cursor.getString(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING));
			long localId = cursor.getLong(cursor.getColumnIndex(DatabaseContract.MedTableInfo._ID));
			FhirContext fhirContext = FhirServices.getFhirServices().getFhirContextInstance();
			MedicationStatement medStatement = (MedicationStatement) fhirContext.newJsonParser().parseResource(jsonMedString);
			return new MyMedicationStatement((int) localId, medStatement);
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
		MyMedicationStatement medStatement = (MyMedicationStatement) getGroup(groupPosition);
		MedicationStatement medStatementFhir = medStatement.getFhirMedStatement();
		CodeableConceptDt codeableConcept = (CodeableConceptDt)medStatementFhir.getMedication();
		String name = codeableConcept.getText();
		TextView textView = (TextView) result.findViewById(R.id.list_display_name);
		textView.setText(name);

		textView = (TextView) result.findViewById(R.id.list_display_dosage);
		List<MedicationStatement.Dosage> listDosage = medStatementFhir.getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);
		SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
		textView.setText(simpleQuantityDt.getValueElement().getValueAsInteger()+"");

		textView = (TextView) result.findViewById(R.id.list_display_dosage_unit);
		textView.setText(simpleQuantityDt.getUnitElement().toString());

		final RelativeLayout indicator = (RelativeLayout) result.findViewById(R.id.indicator);
		ImageView imageView = (ImageView) indicator.findViewById(R.id.indicator_image);
		if (isExpanded){
			imageView.setImageResource(R.drawable.arrow_up_grey_11dp);
		}else{
			imageView.setImageResource(R.drawable.arrow_down_grey_11dp);
		}

		return result;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
		  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_medication_list_child, null);
		final MyMedicationStatement myMedicationStatement = (MyMedicationStatement) getGroup(groupPosition);
		final MedicationStatement medicationStatement = myMedicationStatement.getFhirMedStatement();
		CodeableConceptDt codeableConceptDt = (CodeableConceptDt) medicationStatement.getReasonForUse();

		if (codeableConceptDt != null) {
			TextView textView = (TextView) result.findViewById(R.id.list_reasonForUse);
			textView.setText(codeableConceptDt.getText());
		}

		TextView textView = (TextView) result.findViewById(R.id.list_Note);
		textView.setText(medicationStatement.getNote());

		Button editButton = (Button) result.findViewById(R.id.button_edit);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OnEditButtonClick(myMedicationStatement.getLocalId());
			}
		});

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
