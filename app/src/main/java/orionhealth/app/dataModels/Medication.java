//       Description: Instance of database
//		 @author:  Bill
package orionhealth.app.dataModels;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;

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

	public void trial(){
		MedicationStatement medicationStatement = new MedicationStatement();
		medicationStatement.setStatus(MedicationStatementStatusEnum.ACTIVE);
		FhirContext ctx = FhirContext.forDstu2();
		String jsonEncoded = ctx.newJsonParser().encodeResourceToString(medicationStatement);
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
