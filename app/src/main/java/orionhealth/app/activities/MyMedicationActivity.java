package orionhealth.app.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.*;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import orionhealth.app.R;
import orionhealth.app.medicationDatabase.*;
import orionhealth.app.medicationDatabase.DatabaseContract.*;

public class MyMedicationActivity extends AppCompatActivity {
	public final static String SELECTED_MED_ID = "orionhealth.app.SELECTED_MED_ID";

	private String[] fromColumns = {MedTableInfo.COLUMN_NAME_NAME, MedTableInfo.COLUMN_NAME_DOSAGE};
	private int[] toViews = {R.id.list_display_name, R.id.list_display_dosage};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_medication);

		Cursor cursor = MedTableOperations.getAllRows(this);
		SimpleCursorAdapter adapter =
		  	new SimpleCursorAdapter(this, R.layout.list_medication_layout, cursor, fromColumns, toViews, 0);
		ListView listView = (ListView) findViewById(R.id.my_medication_list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				Intent intent = new Intent(getBaseContext(), EditMedicationActivity.class);
				intent.putExtra(SELECTED_MED_ID, id);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_my_medication, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.button_add) {
			Intent intent = new Intent(this, AddMedicationActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
