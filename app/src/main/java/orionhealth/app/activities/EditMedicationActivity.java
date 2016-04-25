//       Description:
//		 @author:  Bill

package orionhealth.app.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import orionhealth.app.R;
import orionhealth.app.dataModels.Medication;
import orionhealth.app.fragments.Fragments;
import orionhealth.app.fragments.ListFragments.*;
import orionhealth.app.medicationDatabase.*;

public class EditMedicationActivity extends AppCompatActivity {
	private int mMedicationID;
	private Medication mMedication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_medication);

		Intent intent = getIntent();
		mMedicationID = (int) intent.getLongExtra(MedicationListFragment.SELECTED_MED_ID, 0);
		mMedication = MedTableOperations.getMedication(this, mMedicationID);

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragments.MedicationDetailsFragment medDetailsfragment =
		  		(Fragments.MedicationDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_medication_details);
		medDetailsfragment.populateFields(mMedication);


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
				int dosageInt = Integer.parseInt(updatedDosage);
				Medication updatedMed = new Medication(updatedName, dosageInt);

				if (!mMedication.equals(updatedMed)){
					MedTableOperations.updateMedication(this, mMedicationID, updatedMed);
				};

			} catch (NumberFormatException e) {
				Log.d("hello", "dosage not an int");
			}
		}

		Intent intent = new Intent(this, MyMedicationActivity.class);
		startActivity(intent);
	}
}
