package orionhealth.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import orionhealth.app.R;
import orionhealth.app.activities.main.MyMedicationActivity;

/**
 * Created by bill on 4/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
	public static int NOTIFICATION_ID = 0;
	public static String NOTIFICATION_ID_KEY = "notification_id";
	// activity handle ringtone stop
	// intent send information about medication
	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent startRingToneIntent = new Intent(context, RingToneService.class);
		context.startService(startRingToneIntent);

		Intent takeMedicationIntent = new Intent(context, MedResponseService.class);
		takeMedicationIntent.putExtra(NOTIFICATION_ID_KEY, NOTIFICATION_ID);
		PendingIntent pendingIntentCancel = PendingIntent.getService(context, 0, takeMedicationIntent, 0);

		Notification notification = new NotificationCompat.Builder(context)
		  .setContentTitle("Random title")
		  .setContentText("Random text")
		  .setSmallIcon(R.drawable.arrow_down_grey_11dp)
		  .setPriority(Notification.PRIORITY_HIGH)
		  .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
		  .addAction(R.drawable.pill, "take", pendingIntentCancel)
		  .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MyMedicationActivity.class), 0))
		  .build();

		notificationManager.notify(AlarmReceiver.NOTIFICATION_ID, notification);

	}
}
