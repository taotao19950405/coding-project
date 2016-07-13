//       Description:
//		 @author:  Bill

package orionhealth.app.activities.main;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.dialogFragments.RemoveMedicationDialogFragment;
import orionhealth.app.activities.fragments.fragments.MedicationDetailsFragment;
import orionhealth.app.activities.fragments.listFragments.MedicationListFragment;

public class EditSymptomsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_symptoms);

		LinearLayout context = (LinearLayout) findViewById(R.id.linear_layout_vertical_add_symptoms);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View details_layout = inflater.inflate(R.layout.fragment_symptoms_details, null);
		context.addView(details_layout, 0);

		Intent intent = getIntent();
		int medicationID = intent.getIntExtra(MedicationListFragment.SELECTED_MED_ID, 0);

	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_edit_symptoms, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement

		return super.onOptionsItemSelected(item);
	}

	public void removeMedication(View view){
//		mMedDetailsFragment.removeMedication();
	}

	public void updateMedicationInDatabase(View view){
//		mMedDetailsFragment.updateMedicationInDatabase(this);
	}

	public void saveMedication(View view){}
}
