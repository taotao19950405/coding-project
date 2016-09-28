package orionhealth.app.fhir;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.rest.api.SummaryEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import orionhealth.app.data.dataModels.MyAllergyIntolerance;
import orionhealth.app.data.dataModels.MyCondition;
import orionhealth.app.data.dataModels.MyMedication;
import orionhealth.app.data.medicationDatabase.AllergyTableOperations;
import orionhealth.app.data.medicationDatabase.CondTableOperations;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

import java.util.ArrayList;
import java.util.List;

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

//	Fhir medication service started

	public void sendMedicationToServer(MyMedication resource, Context context){
		SendMedicationToServerTask task = new SendMedicationToServerTask(context);
		task.execute(resource);
	}

	public ArrayList<String> searchMedication(CharSequence constraint) {
		FhirContext fhirContext = getFhirContextInstance();
		IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);
		ArrayList<String> result = new ArrayList<>();

		Bundle response = client
		  .search()
		  .forResource(Medication.class)
		  .where(Medication.CODE.isMissing(false))
		  .count(50)
		  .returnBundle(Bundle.class)
		  .execute();

		while ((response.getLink(Bundle.LINK_NEXT) != null)) {

			List<Bundle.Entry> entryList = response.getEntry();
			for (int i = 0; i < entryList.size(); i++) {
				Bundle.Entry entry = entryList.get(i);
				Medication med = (Medication) entry.getResource();

				List<CodingDt> list = med.getCode().getCoding();

				if (!list.isEmpty()) {
					CodingDt code = list.get(0);

					String medName = code.getDisplay();

					if (medName != null && medName.toLowerCase().contains(constraint.toString().toLowerCase())) {
						result.add(medName);
					}

					if (result.size() == 5) {
						return result;
					}
				}
			}

			response = client.loadPage().next(response).execute();
		}

//		if (response.getLink(Bundle.LINK_NEXT) != null) {
//			response = client.loadPage().next(response).execute();
//			List<Bundle.Entry> entryList2 = response.getEntry();
//			for (int i = 0; i < entryList2.size(); i++) {
//				Bundle.Entry entry = entryList2.get(i);
//				Medication med = (Medication) entry.getResource();
//
//				List<CodingDt> list = med.getCode().getCoding();
//
//				if (!list.isEmpty()) {
//					CodingDt code = list.get(0);
//
//					String medName = code.getDisplay();
//
//					if (medName != null && medName.toLowerCase().contains(constraint.toString().toLowerCase())) {
//						result.add(medName);
//					}
//
//					if (result.size() == 5) {
//						return result;
//					}
//				}
//			}
//		}
		return result;
	}

	private class SendMedicationToServerTask extends AsyncTask<MyMedication, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private SendMedicationToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyMedication... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MyMedication myMedication = params[i];
					MethodOutcome outcome = client.create().resource(myMedication.getFhirMedStatement()).prettyPrint().encodedJson().execute();
					IdDt id = (IdDt) outcome.getId();
					MedicationStatement medicationStatement = myMedication.getFhirMedStatement();
					medicationStatement.setId(id);
					MedTableOperations.getInstance().updateMedication(context, params[i].getLocalId(), myMedication);
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

	public void updateMedicationServer(MyMedication resource, Context context){
		UpdateMedicationToServerTask task = new UpdateMedicationToServerTask(context);
		task.execute(resource);
	}

	private class UpdateMedicationToServerTask extends AsyncTask<MyMedication, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private UpdateMedicationToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyMedication... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MyMedication myMedication = params[i];
					MethodOutcome outcome = client.update().resource(myMedication.getFhirMedStatement())
							.execute();
					IdDt id = (IdDt) outcome.getId();
					myMedication.getFhirMedStatement().setId(id);
					MedTableOperations.getInstance().updateMedication(context, myMedication.getLocalId(), myMedication);
					Log.d("UPDATE SERVER", "Got Id " + id);
					outcomeMessage = "Update the Server " +id;
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				outcomeMessage = "Failed to Update to Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}


	public void inactiveMedication(MedicationStatement resource, Context context){
		inactiveMedicationToServerTask task = new inactiveMedicationToServerTask(context);
		task.execute(resource);
	}

	private class inactiveMedicationToServerTask extends AsyncTask<MedicationStatement, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private inactiveMedicationToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MedicationStatement... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					params[i].setStatus(MedicationStatementStatusEnum.COMPLETED);
					MethodOutcome outcome = client.update().resource(params[i])
							.execute();
					IdDt id = (IdDt) outcome.getId();
					Log.d("INACTIVE", "Got Id " + id);
					outcomeMessage = "Disable the Medication " +id + "In Fhir server";
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				outcomeMessage = "Failed to Update to Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}

//	Fhir medication service finished

//	Fhir condition service started
	public void  sendConditionToServer(MyCondition resource, Context context){
		SendConditionToServerTask task = new SendConditionToServerTask(context);
		task.execute(resource);
	}

	private class SendConditionToServerTask extends AsyncTask<MyCondition, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private SendConditionToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyCondition... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MethodOutcome outcome = client.create().resource(params[i].getFhirCondition()).prettyPrint().encodedJson().execute();
					IdDt id = (IdDt) outcome.getId();
					Condition m = CondTableOperations.getInstance().getCondition(context, params[i].getLocalId());
					m.setId(id);
					CondTableOperations.getInstance().updateCondition(context, params[i].getLocalId(), m);
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

	public void  updateConditionServer(MyCondition resource, Context context){
		UpdateConditionToServerTask task = new UpdateConditionToServerTask(context);
		task.execute(resource);
	}

	private class UpdateConditionToServerTask extends AsyncTask<MyCondition, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private UpdateConditionToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyCondition... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MethodOutcome outcome = client.update().resource(params[i].getFhirCondition())
							.execute();
					IdDt id = (IdDt) outcome.getId();
					Condition m = CondTableOperations.getInstance().getCondition(context, params[i].getLocalId());
					m.setId(id);
					CondTableOperations.getInstance().updateCondition(context, params[i].getLocalId(), m);
					Log.d("UPDATE SERVER", "Got Id " + id);
					outcomeMessage = "Update the Server " +id;
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				outcomeMessage = "Failed to Update to Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}
//	Fhir Condition service finished

//	Fhir AllergyIntolerance service started
	public void  sendAllergyToServer(MyAllergyIntolerance resource, Context context){
		SendAllergyToServerTask task = new SendAllergyToServerTask(context);
		task.execute(resource);
	}

	private class SendAllergyToServerTask extends AsyncTask<MyAllergyIntolerance, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private SendAllergyToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyAllergyIntolerance... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MethodOutcome outcome = client.create().resource(params[i].getFhirAllergyIntolerance()).prettyPrint().encodedJson().execute();
					IdDt id = (IdDt) outcome.getId();
					AllergyIntolerance m = AllergyTableOperations.getInstance().getAllergyIntolerance(context, params[i].getLocalId());
					m.setId(id);
					AllergyTableOperations.getInstance().updateAllergy(context, params[i].getLocalId(), m);
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

	public void  updateAllergyServer(MyAllergyIntolerance resource, Context context){
		UpdateAllergyToServerTask task = new UpdateAllergyToServerTask(context);
		task.execute(resource);
	}

	private class UpdateAllergyToServerTask extends AsyncTask<MyAllergyIntolerance, Integer, Void> {

		private Context context;
		private String outcomeMessage;

		private UpdateAllergyToServerTask(Context context){
			this.context = context;
		}

		protected Void doInBackground(MyAllergyIntolerance... params) {
			FhirContext fhirContext = getFhirContextInstance();
			IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);

			try {
				for (int i = 0; i < params.length; i++) {
					MethodOutcome outcome = client.update().resource(params[i].getFhirAllergyIntolerance())
							.execute();
					IdDt id = (IdDt) outcome.getId();
					AllergyIntolerance m = AllergyTableOperations.getInstance().getAllergyIntolerance(context, params[i].getLocalId());
					m.setId(id);
					AllergyTableOperations.getInstance().updateAllergy(context, params[i].getLocalId(), m);
					Log.d("UPDATE SERVER", "Got Id " + id);
					outcomeMessage = "Update the Server " +id;
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				outcomeMessage = "Failed to Update to Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}
//	Fhir Allergy service finished
}
