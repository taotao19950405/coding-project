package orionhealth.app.services;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bill on 2/07/16.
 */
public class DateService {
	private Format format;
	private final String simpleFormat = "EE dd/M/yyyy";

	public DateService() {
		format = new SimpleDateFormat(simpleFormat);
	}

	public String formatToString(Date date) {
		if (date != null) {
			return format.format(date);
		}
		return "";
	}

	public Date parseDate(String dateString) {
		try {
			return (Date) format.parseObject(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
