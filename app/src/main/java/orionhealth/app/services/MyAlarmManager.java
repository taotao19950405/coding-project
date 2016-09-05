package orionhealth.app.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.LinkedList;

/**
 * Created by bill on 20/08/16.
 */
public class MyAlarmManager extends BroadcastReceiver {
	public static String NOTIFICATION_ID_KEY = "notification_id";
	public static String BUNDLE_KEY = "medication";
	public static String PARCEL_KEY = "parcel";


	private LinkedList<Long> times;

	@Override
	public void onReceive(Context context, Intent intent) {

	}
}
