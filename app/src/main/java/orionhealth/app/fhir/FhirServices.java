package orionhealth.app.fhir;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Created by bill on 1/05/16.
 */
public final class FhirServices {
	private static FhirServices sFhirServices;
	private FhirContext mFhirContext;
	private String mServerBase = "http://fhirtest.uhn.ca/baseDstu2";

	private FhirServices(){
	}

	public static FhirServices getsFhirServices() {
		if (sFhirServices == null){
			sFhirServices = new FhirServices();
			return sFhirServices;
		}else{
			return sFhirServices;
		}
	}

	private FhirContext getFhirContextInstance() {
		if (mFhirContext == null) {
			mFhirContext = FhirContext.forDstu2();
			return mFhirContext;
		}else{
			return mFhirContext;
		}
	}

	public String toJsonString(IBaseResource resource){
		FhirContext fhirContext = getFhirContextInstance();
		return fhirContext.newJsonParser().encodeResourceToString(resource);
	}

	public IBaseResource toResource(String jsonString){
		FhirContext fhirContext = getFhirContextInstance();
		return fhirContext.newJsonParser().parseResource(jsonString);
	}

	public void  sendToServer(IResource resource, Context context){
		SendResourceToServerTask task = new SendResourceToServerTask(context);
		task.execute(resource);
	}

	private class SendResourceToServerTask extends AsyncTask<IResource, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private SendResourceToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(IResource... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {

				for (int i = 0; i < params.length; i++) {
					MethodOutcome outcome = client.create().resource(params[i]).prettyPrint().encodedJson().execute();
					IdDt id = (IdDt) outcome.getId();
					Log.d("SENT TO SERVER", "Got Id " + id);
					outcomeMessage = "Sent to Server " +id;
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				outcomeMessage = "Failed to Send to Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}
}
