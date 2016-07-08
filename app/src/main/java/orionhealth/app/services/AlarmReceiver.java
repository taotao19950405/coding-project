package orionhealth.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import orionhealth.app.R;
import orionhealth.app.activities.main.MyMedicationActivity;

/**
 * Created by bill on 4/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new NotificationCompat.Builder(context)
		  .setContentTitle("Random title")
		  .setContentText("Random text")
		  .setSmallIcon(R.drawable.arrow_down_grey_11dp)
		  .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MyMedicationActivity.class), 0))
		  .build();

		notificationManager.notify(0, notification);

		Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Ringtone ringtoneSound = RingtoneManager.getRingtone(context, ringtoneUri);

		if (ringtoneSound != null) {
			ringtoneSound.play();
		}
	}
}
