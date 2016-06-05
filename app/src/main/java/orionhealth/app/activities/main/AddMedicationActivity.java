//       Description:
//		 @author:  Bill
package orionhealth.app.activities.main;

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
import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.fhir.FhirServices;

import java.util.LinkedList;
import java.util.List;

public class AddMedicationActivity extends AppCompatActivity {

	private EditText mNameTextField;
	private EditText mDosageTextField;
	private Spinner mDosageUnitSelector;
	private EditText mReasonTextField;
	private EditText mInstructionsTextField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_medication);

		mNameTextField = (EditText) findViewById(R.id.edit_text_name);
		mDosageTextField = (EditText) findViewById(R.id.edit_text_dosage);
		mDosageUnitSelector = (Spinner) findViewById(R.id.unit_spinner);
		mReasonTextField = (EditText) findViewById(R.id.edit_text_reasonForUse);
		mInstructionsTextField = (EditText) findViewById(R.id.edit_text_instructions);
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
		//Do something in response to clicking add button
		String name = mNameTextField.getText().toString();
		String dosage = mDosageTextField.getText().toString();
		String spinnerValue = mDosageUnitSelector.getSelectedItem().toString();
		String reasonForUse = mReasonTextField.getText().toString();
		String instructions = mInstructionsTextField.getText().toString();

		MedicationStatement medicationStatement;
		try {
			medicationStatement = createMedStatement(name, dosage, spinnerValue, reasonForUse, instructions);
			MedTableOperations.getInstance().addToMedTable(this, medicationStatement);
			FhirServices.getsFhirServices().sendToServer(medicationStatement);
			Intent intent = new Intent(this, MyMedicationActivity.class);
			startActivity(intent);
		} catch (NoNameException e) {
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
		} catch (NoDosageException e) {
			Toast.makeText(this, "Please enter a dosage", Toast.LENGTH_SHORT).show();
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Please enter a valid dosage", Toast.LENGTH_SHORT).show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private MedicationStatement createMedStatement(String name, String dosage, String unit,
												   String reasonForUse, String note) throws Exception {
		if (!name.equals("")) {
			if (!dosage.equals("")) {
				Long dosageLong = Long.parseLong(dosage);
				MedicationStatement medicationStatement = new MedicationStatement();
				medicationStatement.setMedication(new CodeableConceptDt().setText(name));
				medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);
				ResourceReferenceDt patientRef = new ResourceReferenceDt().setDisplay("LOCAL");
				medicationStatement.setPatient(patientRef);
				medicationStatement.setReasonForUse(new CodeableConceptDt().setText(reasonForUse));
				medicationStatement.setNote(note);
				MedicationStatement.Dosage dosageFhir = new MedicationStatement.Dosage();
				SimpleQuantityDt simpleQuantityDt = new SimpleQuantityDt(dosageLong);
				simpleQuantityDt.setUnit(unit);
				dosageFhir.setQuantity(simpleQuantityDt);
				List<MedicationStatement.Dosage> listDosage = new LinkedList<MedicationStatement.Dosage>();
				listDosage.add(dosageFhir);
				medicationStatement.setDosage(listDosage);
				return medicationStatement;
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
