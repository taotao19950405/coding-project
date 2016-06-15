//       Description:
//		 @author:  Bill

package orionhealth.app.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
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
import orionhealth.app.activities.fragments.dialogFragments.RemoveMedicationDialogFragment;
import orionhealth.app.activities.fragments.fragments.MedicationDetailsFragment;
import orionhealth.app.activities.fragments.listFragments.MedicationListFragment;
import orionhealth.app.data.dataModels.Unit;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

public class EditMedicationActivity extends AppCompatActivity implements RemoveMedicationDialogFragment.RemoveMedDialogListener {
	private int mMedicationID;
	private MedicationStatement mMedication;

	private EditText mNameTextField;
	private EditText mDosageTextField;
	private Spinner mDosageUnitSelector;
	private EditText mReasonTextField;
	private EditText mNotesTextField;

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

		mNameTextField = (EditText) findViewById(R.id.edit_text_name);
		mDosageTextField = (EditText) findViewById(R.id.edit_text_dosage);
		mDosageUnitSelector = (Spinner) findViewById(R.id.unit_spinner);
		mReasonTextField = (EditText) findViewById(R.id.edit_text_reasonForUse);
		mNotesTextField = (EditText) findViewById(R.id.edit_text_instructions);

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
		DialogFragment removeMedDialogue = new RemoveMedicationDialogFragment();
		removeMedDialogue.show(getFragmentManager(), "removeMedication");
	}

	public void updateMedicationInDatabase(View view){
		String name = mNameTextField.getText().toString();
		String dosage = mDosageTextField.getText().toString();
		Unit unit = (Unit) mDosageUnitSelector.getSelectedItem();
		String reasonForUse = mReasonTextField.getText().toString();
		String notes = mNotesTextField.getText().toString();
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

	private void updateMedStatement(String name, String dosage, Unit unit,
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
				simpleQuantityDt.setUnit(unit.name());
				simpleQuantityDt.setCode(unit.ordinal()+"");
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

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		MedTableOperations.getInstance().removeMedication(this, mMedicationID);
		Intent intent = new Intent(this, MyMedicationActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {

	}

	private class NoNameException extends Exception {

	}

	private class NoDosageException extends Exception {

	}
}
