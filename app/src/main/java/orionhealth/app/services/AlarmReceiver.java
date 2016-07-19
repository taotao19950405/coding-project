package orionhealth.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import orionhealth.app.R;
import orionhealth.app.data.dataModels.NotificationParcel;
import orionhealth.app.data.dataModels.Unit;

/**
 * Created by bill on 4/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
	public static int NOTIFICATION_ID = 0;
	public static String NOTIFICATION_ID_KEY = "notification_id";
	public static String MEDICATION_KEY = "medication";
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
		intent.setExtrasClassLoader(context.getClassLoader());

		Bundle bundle = intent.getBundleExtra(MEDICATION_KEY);
		NotificationParcel notificationParcel = bundle.getParcelable("here");

		int drawable;
		if (notificationParcel.getIcon() == Unit.MG.ordinal()) {
			drawable = R.drawable.two_color_pill;
		} else if (notificationParcel.getIcon() == Unit.ML.ordinal()) {
			drawable = R.drawable.medicine;
		} else if (notificationParcel.getIcon() == Unit.SPRAY.ordinal()) {
			drawable = R.drawable.spray_can;
		} else if (notificationParcel.getIcon() == Unit.TABLET.ordinal()) {
			drawable= R.drawable.pill;
		} else {
			drawable = R.drawable.warning;
		}

		Log.d("after send", notificationParcel.getTitle());

		Notification notification = new NotificationCompat.Builder(context)
		  .setContentTitle("Reminder: Take "+notificationParcel.getTitle())
		  .setContentText("Random text")
		  .setSmallIcon(drawable)
		  .setPriority(Notification.PRIORITY_MAX)
		  .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
		  .addAction(R.mipmap.ic_done_all_black_18dp, "take", pendingIntentCancel)
		  .addAction(R.mipmap.ic_clear_black_18dp, "dismiss", pendingIntentCancel)
		  .setDeleteIntent(pendingIntentCancel)
		  .setFullScreenIntent(pendingIntentCancel, true)
//		  .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MyMedicationActivity.class), 0))
		  .build();

		WakeLockService.acquire(context);
		notificationManager.notify(AlarmReceiver.NOTIFICATION_ID, notification);

	}
}
