//       Description:
//		 @author:  Bill
package orionhealth.app.activities.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.fragments.MedicationDetailsFragment;

public class AddMedicationActivity extends AppCompatActivity implements DatePicker.DatePickerListener {

	private MedicationDetailsFragment mMedDetailsFragment;
	public static final String ActivityKey = "Medication";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_medication);

		FragmentManager fragmentManager = getFragmentManager();
		mMedDetailsFragment =
		  (MedicationDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_medication_details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_add_medication, menu);
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

	public void addMedicationToDatabase(View view) {
		try {
			mMedDetailsFragment.addMedicationToDatabase(this);
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("AVTIVITY", ActivityKey);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSetDate(int year, int monthOfYear, int dayOfMonth, String tag) {
		mMedDetailsFragment.onSetDate(year, monthOfYear, dayOfMonth, tag);
	}

	@Override
	public void onCancelDate() {
		mMedDetailsFragment.onCancelDate();
	}
}
