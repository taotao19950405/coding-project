package orionhealth.app.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by bill on 11/10/16.
 */
public class resetAlarmService extends IntentService {

	public resetAlarmService() {
		super("RESET_ALARM_SERVICE");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}
