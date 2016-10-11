//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.data.dataModels;

import android.os.Build;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.primitive.DateTimeDt;

import java.util.List;

/**
 * Created by bill on 8/04/16.
 */
public class MyMedication {
	private int mLocalId;
	private MedicationStatement mFhirMedStatement;
	private Boolean mReminderSet;
	private AlarmPackage mAlarmPackage;

	public MyMedication(){}

	public MyMedication(int localId, MedicationStatement fhirMedStatement, Boolean mReminderSet) {
		this.mLocalId = localId;
		this.mFhirMedStatement = fhirMedStatement;
		this.mReminderSet = mReminderSet;
	}

	public void createAlarmPackage() {
		List<MedicationStatement.Dosage> listDosage = mFhirMedStatement.getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);

		TimingDt timingDt = dosage.getTiming();
		if (!timingDt.getEvent().isEmpty() && mReminderSet) {
			mAlarmPackage = new AlarmPackage();
			DateTimeDt dateTimeDt = timingDt.getEvent().get(0);
			TimingDt.Repeat repeat = timingDt.getRepeat();
			mAlarmPackage.setHour(dateTimeDt.getHour());
			mAlarmPackage.setMinute(dateTimeDt.getMinute());
			mAlarmPackage.setDailyNumOfAlarmsTime(repeat.getFrequency());
			mAlarmPackage.setTimeIntervalToNextAlarm(repeat.getPeriod().intValue());
		}
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	};

	public int getLocalId(){
		return mLocalId;
	}

	public MedicationStatement getFhirMedStatement() {
		return mFhirMedStatement;
	}

	public Boolean getReminderSet() {return mReminderSet; }

	public AlarmPackage getAlarmPackage() { return  mAlarmPackage;}

	public void setLocalId(int localId){
		this.mLocalId = localId;
	}

	public void setFhirMedStatement(MedicationStatement medStatement) {
		this.mFhirMedStatement = medStatement;
	}

	public void setReminderSet(Boolean reminderSet) {this.mReminderSet = reminderSet; }

	public void setAlarmPackage(AlarmPackage alarmPackage) {
		this.mAlarmPackage = alarmPackage;
	}
}
