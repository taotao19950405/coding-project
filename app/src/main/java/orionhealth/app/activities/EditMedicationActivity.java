//       Description:
//		 @author:  Bill

package orionhealth.app.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.layouts.fragments.fragments.MedicationDetailsFragment;
import orionhealth.app.layouts.fragments.listFragments.MedicationListFragment;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

public class EditMedicationActivity extends AppCompatActivity {
	private int mMedicationID;
	private MedicationStatement mMedication;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_medication);

		Intent intent = getIntent();
		mMedicationID = (int) intent.getLongExtra(MedicationListFragment.SELECTED_MED_ID, 0);
		mMedication = MedTableOperations.getMedicationStatement(this, mMedicationID);

		FragmentManager fragmentManager = getSupportFragmentManager();
		MedicationDetailsFragment medDetailsFragment =
		  		(MedicationDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_medication_details);
		medDetailsFragment.populateFields(mMedication);

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

	public void removeMedicationFromDatabase(View view){
		MedTableOperations.removeMedication(this, mMedicationID);
		Intent intent = new Intent(this, MyMedicationActivity.class);
		startActivity(intent);
	}

	public void updateMedicationInDatabase(View view){
		EditText nameEditTextField = (EditText) findViewById(R.id.edit_text_name);
		String updatedName = nameEditTextField.getText().toString();
		EditText dosageEditTextField = (EditText) findViewById(R.id.edit_text_dosage);
		String updatedDosage = dosageEditTextField.getText().toString();

		if (!(updatedName.equals("") || updatedDosage.equals(""))){
			try {
//				int dosageInt = Integer.parseInt(updatedDosage);

				CodeableConceptDt codeableConceptDt = (CodeableConceptDt) mMedication.getMedication();
				codeableConceptDt.setText(updatedName);
				MedTableOperations.updateMedication(this, mMedicationID, mMedication);

			} catch (NumberFormatException e) {
				Log.d("hello", "dosage not an int");
			}
		}

		Intent intent = new Intent(this, MyMedicationActivity.class);
		startActivity(intent);
	}
}
