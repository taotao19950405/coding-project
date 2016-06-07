package orionhealth.app.activities.fragments.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.data.dataModels.MyMedicationStatement;
import orionhealth.app.data.dataModels.Unit;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.activities.external.AnimatedExpandableListView;

import java.util.List;

/**
 * Created by bill on 4/05/16.
 */
public class MyExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
	private Context mContext;
	private Cursor mCursor;

	public MyExpandableListAdapter(Context context) {
		this.mContext = context;
	}
	public MyExpandableListAdapter(Context context, Cursor cursor){
		this.mContext = context;
		this.mCursor = cursor;
	}

	public void OnEditButtonClick(int medicationLocalId){

	}


	@Override
	public int getGroupCount() {
		return mCursor.getCount();
	}


	@Override
	public Object getGroup(int groupPosition) {
		if (mCursor.moveToPosition(groupPosition)) {
			long localId = mCursor.getLong(mCursor.getColumnIndex(DatabaseContract.MedTableInfo._ID));
			String jsonMedString =
			  		mCursor.getString(mCursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING));
			MedicationStatement medStatement =
			  		(MedicationStatement)FhirServices.getsFhirServices().toResource(jsonMedString);
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
	public View getGroupView(int groupPosition, final boolean isExpanded, View convertView,
							 ViewGroup parent) {
		LayoutInflater inflater =
		  		(LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_medication_list_group, null);
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

		String unitIdString = simpleQuantityDt.getCode();
		textView = (TextView) result.findViewById(R.id.list_display_dosage_unit);
		ImageView imageView = (ImageView) result.findViewById(R.id.medication_icon);
		if (unitIdString != null) {
			int unitId = Integer.parseInt(unitIdString);
			Unit unit = Unit.values()[unitId];
			textView.setText(unit.getName());
			if (unitId == Unit.MG.ordinal()) {
				imageView.setImageResource(R.drawable.two_color_pill);
			} else if (unitId == Unit.ML.ordinal()) {
				imageView.setImageResource(R.drawable.medicine);
			} else if (unitId == Unit.SPRAY.ordinal()) {
				imageView.setImageResource(R.drawable.spray_can);
			} else if (unitId == Unit.TABLET.ordinal()) {
				imageView.setImageResource(R.drawable.pill);
			} else {
				imageView.setImageResource(R.drawable.warning);
			}

		} else {
			textView.setText(simpleQuantityDt.getUnit());
			imageView.setImageResource(R.drawable.warning);
		}


		final RelativeLayout indicator = (RelativeLayout) result.findViewById(R.id.indicator);
		imageView = (ImageView) indicator.findViewById(R.id.indicator_image);

		if (isExpanded) {
			imageView.setImageResource(R.drawable.arrow_up_grey_11dp);
		} else {
			imageView.setImageResource(R.drawable.arrow_down_grey_11dp);
		}

		return result;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
								 View convertView, ViewGroup parent) {
		LayoutInflater inflater =
		  		(LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_medication_list_child, null);
		final MyMedicationStatement myMedicationStatement = (MyMedicationStatement) getGroup(groupPosition);
		final MedicationStatement medicationStatement = myMedicationStatement.getFhirMedStatement();
		CodeableConceptDt codeableConceptDt = (CodeableConceptDt) medicationStatement.getReasonForUse();

		if (codeableConceptDt == null) {
			LinearLayout linearLayout = (LinearLayout) result.findViewById(R.id.display_reasonForUse);
			linearLayout.setVisibility(View.GONE);
		} else {
			TextView textView = (TextView) result.findViewById(R.id.list_reasonForUse);
			textView.setText(codeableConceptDt.getText());
		}

		if (medicationStatement.getNote() == null) {
			LinearLayout linearLayout = (LinearLayout) result.findViewById(R.id.display_Note);
			linearLayout.setVisibility(View.GONE);
		} else {
			TextView textView = (TextView) result.findViewById(R.id.list_Note);
			textView.setText(medicationStatement.getNote());
		}

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
