//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.data.dataModels;

import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;

/**
 * Created by bill on 8/04/16.
 */
public class MyMedicationStatement {
	private int mLocalId;
	private MedicationStatement mFhirMedStatement;

	public MyMedicationStatement(int mLocalId, MedicationStatement mFhirMedStatement) {
		this.mLocalId = mLocalId;
		this.mFhirMedStatement = mFhirMedStatement;
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
