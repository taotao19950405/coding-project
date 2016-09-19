package orionhealth.app.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import orionhealth.app.R;
import orionhealth.app.fhir.FhirServices;
import orionhealth.app.services.AlarmSetter;
import orionhealth.app.services.DateService;
import orionhealth.app.services.RingToneService;

import java.util.Calendar;

public class TakeMedicationActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial);
		Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		win.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Intent startRingToneIntent = new Intent(this, RingToneService.class);
		startService(startRingToneIntent);

		int medId = getIntent().getIntExtra(AlarmSetter.MED_ID_KEY, -1);

		String jsonMedString = getIntent().getStringExtra(AlarmSetter.JSON_STRING_KEY);
		MedicationStatement medStatement =
		  		(MedicationStatement) FhirServices.getsFhirServices().toResource(jsonMedString);

		CodeableConceptDt codeableConceptDt =
		  (CodeableConceptDt) medStatement.getMedication();

		long alarmTime = getIntent().getLongExtra(AlarmSetter.ALARM_TIME_KEY, -1);

		DateService dateService = new DateService();
		dateService.setFormat(DateService.FLAG_TIME_FORMAT);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(alarmTime);

		String timeString = dateService.formatToString(calendar.getTime());

		TextView medTitleTextView = (TextView) findViewById(R.id.med_title_text_view);
		medTitleTextView.setText(codeableConceptDt.getText());

		TextView medTimeTextView = (TextView) findViewById(R.id.med_time_text_view);
		medTimeTextView.setText(timeString);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_trial, menu);
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

	public void takeMedication(View view) {
		Intent service = new Intent(this, RingToneService.class);
		getApplicationContext().stopService(service);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
