package orionhealth.app.dataModels;

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

	public String getName() {
		return mName;
	}

	public int getDosage() {
		return mDosage;
	}
}
