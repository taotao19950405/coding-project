package orionhealth.app.fhir;

import android.util.Log;
import ca.uhn.fhir.context.FhirContext;

/**
 * Created by bill on 1/05/16.
 */
public final class FhirServices {
	private static FhirContext fhirContext;

	private FhirServices(){}

	public static FhirContext getFhirContextInstance(){
		if (fhirContext == null) {
			fhirContext = FhirContext.forDstu2();
			return fhirContext;
		}else{
			return fhirContext;
		}
	}
}
