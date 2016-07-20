package orionhealth.app.activities.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import orionhealth.app.R;
import orionhealth.app.data.dataModels.MyCondition;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.fhir.FhirServices;

/**
 * Created by Lu on 19/07/16.
 */
public class MySimpleListAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;

    public MySimpleListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }


    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
	public Object getItem (int position) {
		if (mCursor.moveToPosition(position)) {
            long localId = mCursor.getLong(mCursor.getColumnIndex(DatabaseContract.CondTableInfo._ID));
            String jsonMedString =
                    mCursor.getString(mCursor.getColumnIndex(DatabaseContract.CondTableInfo.COLUMN_NAME_JSON_STRING));
            Condition condition =
                    (Condition) FhirServices.getsFhirServices().toResource(jsonMedString);
			return new MyCondition((int) localId, condition);
		}else {
			return null;
		}
	}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_condition_list_items, null);
        // Find fields to populate in inflated template
        TextView display_condition = (TextView) view.findViewById(R.id.list_display_condition);
        TextView display_date = (TextView) view.findViewById(R.id.list_display_date);

        // Extract properties from cursor
        MyCondition mCondtion = (MyCondition) getItem(position);
        Condition conditionFhir = mCondtion.getFhirCondition();
        CodeableConceptDt codeableConcept = conditionFhir.getCode();
        String condition_string = codeableConcept.getText();

        Date dateinfo = conditionFhir.getDateRecorded();
        String date_string = String.valueOf(dateinfo.getTime());

        // Populate fields with extracted properties
        display_condition.setText(condition_string);
        display_date.setText(date_string);

        return view;
    }
}
