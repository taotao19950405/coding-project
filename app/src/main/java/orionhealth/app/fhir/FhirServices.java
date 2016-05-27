package orionhealth.app.fhir;

import android.os.AsyncTask;
import android.util.Log;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Created by bill on 1/05/16.
 */
public final class FhirServices {
	private static FhirServices fhirServices;
	private FhirContext fhirContext;
	private String serverBase = "http://fhirtest.uhn.ca/baseDstu2";

	private FhirServices(){
	}

	public static FhirServices getFhirServices() {
		if (fhirServices == null){
			fhirServices = new FhirServices();
			return fhirServices;
		}else{
			return fhirServices;
		}
	}

	public FhirContext getFhirContextInstance() {
		if (fhirContext == null) {
			fhirContext = FhirContext.forDstu2();
			return fhirContext;
		}else{
			return fhirContext;
		}
	}

	public String toJsonString(IResource resource){
		FhirContext fhirContext = getFhirContextInstance();
		return fhirContext.newJsonParser().encodeResourceToString(resource);
	}

	public void  sendToServer(IResource resource){
		SendResourceToServerTask task = new SendResourceToServerTask();
		task.execute(resource);
	}

	private class SendResourceToServerTask extends AsyncTask<IResource, Integer, Void> {

		protected Void doInBackground(IResource... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(serverBase);

			for (int i = 0; i < params.length; i++) {
				MethodOutcome outcome = client.create().resource(params[i]).prettyPrint().encodedJson().execute();
				IdDt id = (IdDt) outcome.getId();
				Log.d("SENT TO SERVER", "Got Id " + id);
				return null;
			}
			return null;
		}

	}
}
