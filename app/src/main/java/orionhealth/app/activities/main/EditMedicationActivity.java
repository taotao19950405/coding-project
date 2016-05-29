//       Description:
//		 @author:  Bill

package orionhealth.app.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

import android.widget.Toast;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.activities.fragments.fragments.MedicationDetailsFragment;
import orionhealth.app.activities.fragments.listFragments.MedicationListFragment;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

public class EditMedicationActivity extends AppCompatActivity {
	private int mMedicationID;
	private MedicationStatement mMedication;

	private EditText nameTextField;
	private EditText dosageTextField;
	private Spinner dosageUnitSelector;
	private EditText reasonTextField;
	private EditText notesTextField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_medication);

		Intent intent = getIntent();
		mMedicationID = intent.getIntExtra(MedicationListFragment.SELECTED_MED_ID, 0);
		mMedication = MedTableOperations.getInstance().getMedicationStatement(this, mMedicationID);

		FragmentManager fragmentManager = getSupportFragmentManager();
		MedicationDetailsFragment medDetailsFragment =
		  		(MedicationDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_medication_details);
		medDetailsFragment.populateFields(mMedication);

		nameTextField = (EditText) findViewById(R.id.edit_text_name);
		dosageTextField = (EditText) findViewById(R.id.edit_text_dosage);
		dosageUnitSelector = (Spinner) findViewById(R.id.unit_spinner);
		reasonTextField = (EditText) findViewById(R.id.edit_text_reasonForUse);
		notesTextField = (EditText) findViewById(R.id.edit_text_instructions);

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
		MedTableOperations.getInstance().removeMedication(this, mMedicationID);
		Intent intent = new Intent(this, MyMedicationActivity.class);
		startActivity(intent);
	}

	public void updateMedicationInDatabase(View view){
		String name = nameTextField.getText().toString();
		String dosage = dosageTextField.getText().toString();
		String unit = dosageUnitSelector.getSelectedItem().toString();
		String reasonForUse = reasonTextField.getText().toString();
		String notes = notesTextField.getText().toString();
		try {
			updateMedStatement(name, dosage, unit, reasonForUse, notes);
			MedTableOperations.getInstance().updateMedication(this, mMedicationID, mMedication);
			Intent intent = new Intent(this, MyMedicationActivity.class);
			startActivity(intent);
		} catch (NoNameException e){
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
		} catch (NoDosageException e){
			Toast.makeText(this, "Please enter a dosage", Toast.LENGTH_SHORT).show();
		} catch (NumberFormatException e){
			Toast.makeText(this, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void updateMedStatement(String name, String dosage, String unit,
									String reasonForUse, String note) throws Exception {
		if (!name.equals("")) {
			if (!dosage.equals("")) {
				Long dosageLong = Long.parseLong(dosage);
				CodeableConceptDt codeableConceptDt = (CodeableConceptDt) mMedication.getMedication();
				codeableConceptDt.setText(name);
				codeableConceptDt = (CodeableConceptDt) mMedication.getReasonForUse();
				if (codeableConceptDt == null){
					codeableConceptDt = new CodeableConceptDt();
				}
				codeableConceptDt.setText(reasonForUse);
				mMedication.setReasonForUse(codeableConceptDt);
				mMedication.setNote(note);
				MedicationStatement.Dosage dosageFhir = new MedicationStatement.Dosage();
				SimpleQuantityDt simpleQuantityDt = new SimpleQuantityDt(dosageLong);
				simpleQuantityDt.setUnit(unit);
				dosageFhir.setQuantity(simpleQuantityDt);
				List<MedicationStatement.Dosage> listDosage = new LinkedList<MedicationStatement.Dosage>();
				listDosage.add(dosageFhir);
				mMedication.setDosage(listDosage);
			} else {
				throw new NoDosageException();
			}
		} else {
			throw new NoNameException();
		}

	}

	private class NoNameException extends Exception {

	}

	private class NoDosageException extends Exception {

	}
}
