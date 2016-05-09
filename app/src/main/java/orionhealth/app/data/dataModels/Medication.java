//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.data.dataModels;

/**
 * Created by bill on 8/04/16.
 */
public class Medication {
	private String mName;
	private int mDosage;

	public Medication(String name, int dosage) {
		this.mName = name;
		this.mDosage = dosage;
	}

	@Override
	public boolean equals(Object obj) {
		Medication med = (Medication) obj;
		return (mName.equals(med.mName) && mDosage == med.mDosage);
	};

	public String getName(){
		return mName;
	}

	public int getDosage() {
		return mDosage;
	}
}
