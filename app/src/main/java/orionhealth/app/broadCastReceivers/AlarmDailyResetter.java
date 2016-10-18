package orionhealth.app.broadCastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import orionhealth.app.data.medicationDatabase.MedTableOperations;
import orionhealth.app.services.UpdateUIService;

/**
 * Created by bill on 15/10/16.
 */

public class AlarmDailyResetter extends BroadcastReceiver {

    public static void scheduleAlarmResetter(Context context) {
        Intent intent = new Intent(context, AlarmDailyResetter.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 20, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis() + 2000, AlarmManager.INTERVAL_DAY, alarmPendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RESET", "onReceive");
        MedTableOperations.getInstance().resetMedicationDay(context);
        intent = new Intent(context, UpdateUIService.class);
        context.startService(intent);
    }
}
