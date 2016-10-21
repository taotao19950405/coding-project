//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.data.dataModels;

import android.os.Build;
import android.util.Log;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;

import java.util.List;

import static android.content.ContentValues.TAG;

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

			if (repeat.getPeriodUnits().equals(UnitsOfTimeEnum.MIN.getCode())) {
				mAlarmPackage.setTimeIntervalToNextAlarm(repeat.getPeriod().intValue());
			} else if (repeat.getPeriodUnits().equals(UnitsOfTimeEnum.H.getCode())) {
				mAlarmPackage.setTimeIntervalToNextAlarm(repeat.getPeriod().intValue() * 60);
			} else {
				Log.d(TAG, repeat.getPeriodUnits());
				Log.d(TAG, UnitsOfTimeEnum.H.toString());
			}
		}
	}

	public String getName() {
		CodeableConceptDt codeableConcept = (CodeableConceptDt) getFhirMedStatement().getMedication();
		return codeableConcept.getText();
	}

	public int getDosage() {
		List<MedicationStatement.Dosage> listDosage = getFhirMedStatement().getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);
		SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
		return simpleQuantityDt.getValueElement().getValueAsInteger();
	}

	public int getDosageUnitID() {
		List<MedicationStatement.Dosage> listDosage = getFhirMedStatement().getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);
		SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
		String unitIdString = simpleQuantityDt.getCode();
		return Integer.parseInt(unitIdString);

	}

	public String getDosageUnit() {
		List<MedicationStatement.Dosage> listDosage = getFhirMedStatement().getDosage();
		MedicationStatement.Dosage dosage = listDosage.get(0);
		SimpleQuantityDt simpleQuantityDt = (SimpleQuantityDt) dosage.getQuantity();
		return simpleQuantityDt.getUnit();
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
