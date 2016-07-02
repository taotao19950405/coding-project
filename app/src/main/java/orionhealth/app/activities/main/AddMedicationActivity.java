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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import orionhealth.app.R;
import orionhealth.app.activities.fragments.dialogFragments.DatePicker;
import orionhealth.app.activities.fragments.fragments.MedicationDetailsFragment;
import orionhealth.app.data.dataModels.Unit;
import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.fhir.FhirServices;

import java.util.LinkedList;
import java.util.List;

public class AddMedicationActivity extends AppCompatActivity implements DatePicker.DatePickerListener {

	private MedicationDetailsFragment mMedDetailsFragment;

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
		mMedDetailsFragment.addMedicationToDatabase(this);
	}

	@Override
	public void onSetStartDate(int year, int monthOfYear, int dayOfMonth) {
		mMedDetailsFragment.onSetStartDate(year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onSetEndDate(int year, int monthOfYear, int dayOfMonth) {
		mMedDetailsFragment.onSetEndDate(year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onCancelStartDate() {
		mMedDetailsFragment.onCancelStartDate();
	}
}
