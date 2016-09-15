package orionhealth.app.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedTableInfo;
import orionhealth.app.data.medicationDatabase.DatabaseContract.MedReminderTableInfo;
import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.data.spinnerEnum.MedUptakeStatus;

import java.util.Calendar;

/**
 * Created by bill on 20/08/16.
 */
public class AlarmSetter extends BroadcastReceiver {
	public static final String MED_ID_KEY = "med_id_key";
	public static final String JSON_STRING_KEY = "json_string_key";
	public static final String ALARM_TIME_KEY = "alarm_time_key";
	public static final String REMINDER_SET_KEY = "reminder_set_key";

	@Override
	public void onReceive(Context context, Intent intent) {
		int medId = intent.getIntExtra(MED_ID_KEY, -1);
		Boolean isReminderSet = intent.getBooleanExtra(REMINDER_SET_KEY, false);

		if (isReminderSet) {
			Cursor cursor =
			  		MedTableOperations.getInstance().getMedReminders(context, medId, MedUptakeStatus.PENDING.ordinal());
			Calendar calendar = Calendar.getInstance();
			while (cursor.moveToNext()) {
				String jsonString =
				  cursor.getString(cursor.getColumnIndex(MedTableInfo.COLUMN_NAME_JSON_STRING));
				long alarmTime =
				  cursor.getLong(cursor.getColumnIndex(MedReminderTableInfo.COLUMN_NAME_TIME));

				if (calendar.getTimeInMillis() < alarmTime) {
					intent = new Intent(context, AlarmReceiver.class);
					intent.putExtra(MED_ID_KEY, medId);
					intent.putExtra(JSON_STRING_KEY, jsonString);
					intent.putExtra(ALARM_TIME_KEY, alarmTime);
					PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, medId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
					alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent);
					return;
				}
			}
		} else {
			intent = new Intent(context, AlarmReceiver.class);
			PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, medId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(alarmPendingIntent);
		}

	}
}
