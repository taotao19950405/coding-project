//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.data.dataModels;

import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;

/**
 * Created by Lu on 14/07/16.
 */
public class MySymptoms {
	private int mLocalId;
	private MedicationStatement mFhirMedStatement;

	public MySymptoms(int localId, MedicationStatement fhirMedStatement) {
		this.mLocalId = localId;
		this.mFhirMedStatement = fhirMedStatement;
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
}
