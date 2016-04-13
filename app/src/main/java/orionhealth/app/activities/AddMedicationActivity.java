//       Description:
//		 @author:  Bill
package orionhealth.app.activities;

import android.content.Context;
import android.content.Intent;
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
import orionhealth.app.medicationDatabase.*;

public class AddMedicationActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_medication);
		LinearLayout context = (LinearLayout) findViewById(R.id.linear_layout_vertical_add_medication);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View details_layout = inflater.inflate(R.layout.medication_details, null);
		context.addView(details_layout, 0);
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

	public void addMedication(View view) {
		//Do something in response to clicking add button
		Intent intent = new Intent(this, MyMedicationActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_text_name);
		String name = editText.getText().toString();
		editText = (EditText) findViewById(R.id.edit_text_dosage);
		String dosage = editText.getText().toString();
		if (!(name.equals("") || dosage.equals(""))){
			try {
				int dosageInt = Integer.parseInt(dosage);
				Medication med = new Medication(name, dosageInt);
				MedTableOperations.addToMedTable(this, med);
			} catch (NumberFormatException e) {
				Log.d("hello", "dosage not an int");
			}
		}
		startActivity(intent);
	}
}
