package orionhealth.app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import orionhealth.app.R;
import orionhealth.app.dataModels.Medication;
import orionhealth.app.medicationDatabase.DatabaseOperations;

public class EditMedicationActivity extends AppCompatActivity {
	private int mMedicationID;
	private Medication mMed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_medication);
		LinearLayout context = (LinearLayout) findViewById(R.id.linear_layout_vertical_edit_medication);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View details_layout = inflater.inflate(R.layout.medication_details, null);
		context.addView(details_layout, 0);

		Intent intent = getIntent();
		mMedicationID = (int) intent.getLongExtra(MyMedicationActivity.SELECTED_MED_ID, 0);

		DatabaseOperations dob = new DatabaseOperations(this);
		mMed = dob.getMedication(mMedicationID);

		EditText nameEditTextField = (EditText) findViewById(R.id.edit_text_name);
		nameEditTextField.setText(mMed.getName());
		EditText dosageEditTextField = (EditText) findViewById(R.id.edit_text_dosage);
		dosageEditTextField.setText(""+mMed.getDosage());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_edit_medication, menu);
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
}
