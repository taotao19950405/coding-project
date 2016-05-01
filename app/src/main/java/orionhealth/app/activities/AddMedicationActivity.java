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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import orionhealth.app.R;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.medicationDatabase.MedTableOperations;

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
		String dosage = editText.getText().toString();
		if (!(name.equals("") || dosage.equals(""))){
			try {
				int dosageInt = Integer.parseInt(dosage);
				MedicationStatement medicationStatement = new MedicationStatement();
				medicationStatement.setMedication(new CodeableConceptDt().setText(name));
				medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);
				medicationStatement.setPatient(new ResourceReferenceDt("LOCAL"));
				FhirContext fhirContext = FhirServices.getFhirContextInstance();
				MedTableOperations.addToMedTable(this, fhirContext.newJsonParser().encodeResourceToString(medicationStatement));
			} catch (NumberFormatException e) {
				Log.d("hello", "dosage not an int");
			}
		}
		startActivity(intent);
	}
}
