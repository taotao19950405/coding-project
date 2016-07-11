package orionhealth.app.data.dataModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bill on 11/07/16.
 */
public class NotificationParcel implements Parcelable{
	private String title;
	private String content;
	private int icon;

	public NotificationParcel(String title, String content, int icon) {
		this.title = title;
		this.content = content;
		this.icon = icon;

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

	public void setIcon(int icon) {
		this.icon = icon;
	}

	protected NotificationParcel(Parcel in) {
		title = in.readString();
		content = in.readString();
		icon = in.readInt();
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
		dest.writeString(title);
		dest.writeString(content);
		dest.writeInt(icon);
	}
}
