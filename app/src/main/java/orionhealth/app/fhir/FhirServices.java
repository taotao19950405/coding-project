package orionhealth.app.fhir;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.MedicationStatementStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import orionhealth.app.data.dataModels.MyAllergyIntolerance;
import orionhealth.app.data.dataModels.MyCondition;
import orionhealth.app.data.dataModels.MyMedication;
import orionhealth.app.data.medicationDatabase.AllergyTableOperations;
import orionhealth.app.data.medicationDatabase.CondTableOperations;
import orionhealth.app.data.medicationDatabase.DatabaseContract;
import orionhealth.app.data.medicationDatabase.MedTableOperations;

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

	public void updateMedicationServer(MyMedication resource, Context context){
		UpdateMedicationToServerTask task = new UpdateMedicationToServerTask(context);
		task.execute(resource);
	}

	public void inactiveMedication(MedicationStatement resource, Context context){
		inactiveMedicationToServerTask task = new inactiveMedicationToServerTask(context);
		task.execute(resource);
	}

	//	Fhir condition service started
	public void  sendConditionToServer(MyCondition resource, Context context){
		SendConditionToServerTask task = new SendConditionToServerTask(context);
		task.execute(resource);
	}

	public void  updateConditionServer(MyCondition resource, Context context){
		UpdateConditionToServerTask task = new UpdateConditionToServerTask(context);
		task.execute(resource);
	}

	//	Fhir AllergyIntolerance service started
	public void  sendAllergyToServer(MyAllergyIntolerance resource, Context context){
		SendAllergyToServerTask task = new SendAllergyToServerTask(context);
		task.execute(resource);
	}

//	Fhir medication service finished

	public void  updateAllergyServer(MyAllergyIntolerance resource, Context context){
		UpdateAllergyToServerTask task = new UpdateAllergyToServerTask(context);
		task.execute(resource);
	}

	//	Fhir sync 1 - search whether all the local medication resources has been sent to the server, if not then send.
	public void PushLocalToServer(Context context) {
		//query from database of all the med resources
		Cursor cursor = MedTableOperations.getInstance().getAllRows(context);
		ArrayList<MyMedication> unsentMeds = new ArrayList<MyMedication>();

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			String jsonMedString = cursor.getString(cursor.getColumnIndex(DatabaseContract.MedTableInfo.COLUMN_NAME_JSON_STRING));
			int medID = cursor.getInt(0);
//			Log.d("Local ID" , String.valueOf(medID));
			MedicationStatement medStatement = (MedicationStatement) toResource(jsonMedString);

			if (medStatement.getId().isEmpty()){
				// set alarm boolean as false for convenience
				unsentMeds.add(new MyMedication(medID, medStatement, false));
			}
			cursor.moveToNext();
		}
		System.out.println("The amount of unsent data" + unsentMeds.size());

		String outcomeMessage;
		try {
			for (int j = 0; j < unsentMeds.size(); j++) {
				sendMedicationToServer(unsentMeds.get(j), context);
			}
			outcomeMessage = "All local data is synced.";

		} catch (Exception e) {
			e.printStackTrace();
			outcomeMessage = "Failed to Push";
		}

		Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
	}

	//	Fhir sync 2 - search the patient "mTestO" in the server
	public void PullServerToLocal(Context context){

		FhirContext fhirContext = getFhirContextInstance();
		IGenericClient client = fhirContext.newRestfulGenericClient(mServerBase);
		String outcomeMessage = "Search failed";

		ResourceReferenceDt patientRef = new ResourceReferenceDt();

		try {
			// Perform a search - to be tested
			Bundle results = client
					.search()
					.forResource(MedicationStatement.class)
					.where(MedicationStatement.PATIENT.hasChainedProperty(Patient.NAME.matches().value("mTest0")))
					.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
					.execute();

			System.out.println("Found " + results.getEntry().size() + " patients named 'mTestO'");
			outcomeMessage = "New medication found in server";

		} catch (Exception e) {
			e.printStackTrace();
			outcomeMessage = "Search failed";
		}
		Toast.makeText(context, outcomeMessage, Toast.LENGTH_LONG).show();
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
				outcomeMessage = "Failed to Send to the Server";
			}
			return null;
		}

		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			Toast.makeText(context,outcomeMessage, Toast.LENGTH_LONG).show();
		}
	}
//	Fhir Condition service finished

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
//	Fhir Allergy service finished

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


}
