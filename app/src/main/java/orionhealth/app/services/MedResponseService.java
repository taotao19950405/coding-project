package orionhealth.app.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
		int notificationId = intent.getIntExtra(AlarmReceiver.NOTIFICATION_ID_KEY, 1);
		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(notificationId);
		Intent service = new Intent(getApplicationContext(), RingToneService.class);
		getApplicationContext().stopService(service);
		WakeLockService.release();
	}
}
