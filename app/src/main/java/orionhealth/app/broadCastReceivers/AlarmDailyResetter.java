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
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Calendar calendar = Calendar.getInstance();
//        alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), alarmPendingIntent);
        context.sendBroadcast(intent);
        Log.d("RESET", "scheduleAlarm");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RESET", "onReceive");
        MedTableOperations.getInstance().resetMedicationDay(context);
        intent = new Intent(context, UpdateUIService.class);
        context.startService(intent);
    }
}
