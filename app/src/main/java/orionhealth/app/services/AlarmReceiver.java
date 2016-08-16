package orionhealth.app.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import orionhealth.app.R;
import orionhealth.app.data.dataModels.NotificationParcel;
import orionhealth.app.data.spinnerEnum.MedicationUnit;

import java.util.Calendar;

/**
 * Created by bill on 4/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
	private int notificationId;
	public static String NOTIFICATION_ID_KEY = "notification_id";
	public static String BUNDLE_KEY = "medication";
	public static String PARCEL_KEY = "parcel";
	// activity handle ringtone stop
	// intent send information about medication
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getBundleExtra(BUNDLE_KEY);
		NotificationParcel notificationParcel = bundle.getParcelable(PARCEL_KEY);
		notificationId = notificationParcel.getId();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent startRingToneIntent = new Intent(context, RingToneService.class);
		context.startService(startRingToneIntent);

		Intent takeMedicationIntent = new Intent(context, MedResponseService.class);
		takeMedicationIntent.putExtra(NOTIFICATION_ID_KEY, notificationId);
		PendingIntent pendingIntentCancel = PendingIntent.getService(context, notificationId, takeMedicationIntent, 0);

		int drawable;
		if (notificationParcel.getIcon() == MedicationUnit.MG.ordinal()) {
			drawable = R.drawable.two_color_pill;
		} else if (notificationParcel.getIcon() == MedicationUnit.ML.ordinal()) {
			drawable = R.drawable.medicine;
		} else if (notificationParcel.getIcon() == MedicationUnit.SPRAY.ordinal()) {
			drawable = R.drawable.spray_can;
		} else if (notificationParcel.getIcon() == MedicationUnit.TABLET.ordinal()) {
			drawable= R.drawable.pill;
		} else {
			drawable = R.drawable.warning;
		}

		Notification notification = new NotificationCompat.Builder(context)
		  .setContentTitle("Take "+notificationParcel.getTitle())
		  .setContentText("Medication Reminder")
		  .setSmallIcon(drawable)
		  .setPriority(Notification.PRIORITY_MAX)
		  .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
		  .addAction(R.mipmap.ic_done_all_black_18dp, "take", pendingIntentCancel)
		  .addAction(R.mipmap.ic_clear_black_18dp, "dismiss", pendingIntentCancel)
		  .setDeleteIntent(pendingIntentCancel)
		  .setFullScreenIntent(pendingIntentCancel, true)
//		  .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0))
		  .build();

		WakeLockService.acquire(context);
		PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + notificationParcel.getTimeToNextAlarm(),
		  alarmPendingIntent);
		notificationManager.notify(notificationParcel.getId(), notification);

	}
}
