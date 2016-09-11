package orionhealth.app.services;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import orionhealth.app.activities.adaptors.MedReminderListAdaptor;
import orionhealth.app.activities.main.MainActivity;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

/**
 * Created by bill on 11/07/16.
 */
public class MedResponseService extends IntentService {

	public MedResponseService() {
		super("MED_RESPONSE_SERVICE");
	}

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */

	public MedResponseService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int notificationId = intent.getIntExtra(AlarmSetter.MED_ID_KEY, -1);
		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
		Intent service = new Intent(getApplicationContext(), RingToneService.class);
		getApplicationContext().stopService(service);
		WakeLockService.release();

		long time = intent.getLongExtra(AlarmSetter.ALARM_TIME_KEY, -1);
		MedTableOperations.getInstance().removeSingleReminder(getApplicationContext(), time);

		LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
		intent = new Intent("update");
		broadcaster.sendBroadcast(intent);

	}
}
