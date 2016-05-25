//       Description:
//		 @author:  Bill
package orionhealth.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_medication);
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
		Intent intent = new Intent(this, MyMedicationActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_text_name);
		String name = editText.getText().toString();
		editText = (EditText) findViewById(R.id.edit_text_dosage);
		Spinner spinner = (Spinner) findViewById(R.id.unit_spinner);
		String spinnerValue = spinner.getSelectedItem().toString();
		String dosage = editText.getText().toString();
		editText = (EditText) findViewById(R.id.edit_text_reasonForUse);
		String reasonForUse = editText.getText().toString();
		editText = (EditText) findViewById(R.id.edit_text_instructions);
		String instructions = editText.getText().toString();
		if (!(name.equals("") || dosage.equals(""))){
			try {
				Long dosageLong = Long.parseLong(dosage);
				MedicationStatement medicationStatement = new MedicationStatement();
				medicationStatement.setMedication(new CodeableConceptDt().setText(name));

				medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);

				ResourceReferenceDt patientRef = new ResourceReferenceDt().setDisplay("LOCAL");
				medicationStatement.setPatient(patientRef);

				medicationStatement.setReasonForUse(new CodeableConceptDt().setText(reasonForUse));

				medicationStatement.setNote(instructions);

				MedicationStatement.Dosage dosageFhir = new MedicationStatement.Dosage();
				SimpleQuantityDt simpleQuantityDt = new SimpleQuantityDt(dosageLong);
				simpleQuantityDt.setUnit(spinnerValue);
				dosageFhir.setQuantity(simpleQuantityDt);
				List<MedicationStatement.Dosage> listDosage = new LinkedList<MedicationStatement.Dosage>();
				listDosage.add(dosageFhir);
				medicationStatement.setDosage(listDosage);

				MedTableOperations.getInstance().addToMedTable(this, medicationStatement);
				FhirServices.getFhirServices().sendToServer(medicationStatement);
			} catch (NumberFormatException e) {
				Log.d("hello", "dosage not an int");
			}
		}
		startActivity(intent);
	}
}
