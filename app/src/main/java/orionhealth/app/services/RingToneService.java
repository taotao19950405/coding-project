package orionhealth.app.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by bill on 10/07/16.
 */
public class RingToneService extends Service {
	private boolean isRinging = false;
	private Ringtone ringtone;
//	private final class ServiceHandler extends Handler {
//		public ServiceHandler(Looper looper) {
//			super(looper);
//		}
//		@Override
//		public void handleMessage(Message msg) {
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// Restore interrupt status.
//				Thread.currentThread().interrupt();
//			}
//			// Stop the service using the startId, so that we don't stop
//			// the service in the middle of handling another job
//			stopSelf(msg.arg1);
//		}
//	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);
		if (r != null && !isRinging){
			ringtone = r;
			ringtone.play();
			isRinging = true;
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		ringtone.stop();
		isRinging = false;
		super.onDestroy();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
