package orionhealth.app.data.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bill on 11/07/16.
 */
public class NotificationParcel implements Parcelable{
	private int id;
	private String title;
	private String content;
	private int icon;
	private int timeToNextAlarm;

	public NotificationParcel(int id,String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}


	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public int getIcon() {
		return icon;
	}

	public int getTimeToNextAlarm() {
		return timeToNextAlarm;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public void setTimeIntervalToNextAlarm(int timeInterval) {
		this.timeToNextAlarm = timeInterval;
	}

	protected NotificationParcel(Parcel in) {
		id = in.readInt();
		title = in.readString();
		content = in.readString();
		icon = in.readInt();
		timeToNextAlarm = in.readInt();
	}

	public static final Creator<NotificationParcel> CREATOR = new Creator<NotificationParcel>() {
		@Override
		public NotificationParcel createFromParcel(Parcel in) {
			return new NotificationParcel(in);
		}

		@Override
		public NotificationParcel[] newArray(int size) {
			return new NotificationParcel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeInt(icon);
		dest.writeInt(timeToNextAlarm);
	}
}
