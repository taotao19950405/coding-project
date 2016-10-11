package orionhealth.app.data.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bill on 11/07/16.
 */
public class AlarmPackage {
	private int hour;
	private int minute;
	private int timeToNextAlarm;
	private int dailyNumOfAlarms;


	public AlarmPackage() {
	}


	public int getIntervalTimeToNextAlarm() {
		return timeToNextAlarm;
	}

	public long getDailyNumOfAlarms() {
		return this.dailyNumOfAlarms;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}
//	public long[] getAlarmTimes() { return this.alarmTimes; }


	public void setTimeIntervalToNextAlarm(int timeInterval) {
		this.timeToNextAlarm = timeInterval;
	}

	public void setDailyNumOfAlarmsTime(int dailyNumOfAlarms) {
		this.dailyNumOfAlarms = dailyNumOfAlarms;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}


}
