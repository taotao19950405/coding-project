package orionhealth.app.activities.fragments.fragments;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import orionhealth.app.R;
import android.content.Context;

import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import orionhealth.app.activities.main.AddAllergyActivity;

/**
 * Created by archanakhanal on 16/7/2016.
 */
public class AllergyDetailsFragment extends Fragment {
    private int aAllergyId;
    private AllergyIntolerance aAllergy;

    private EditText aNameTextField;
    private EditText aDetailsTextField;
    private EditText aReactionTextField;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View detailsFragment = inflater.inflate(R.layout.fragment_allergy_details, container, false);

        aNameTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_name_allergy);
        aDetailsTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_details_allergy);
        aReactionTextField = (EditText) detailsFragment.findViewById(R.id.edit_text_reaction_allergy);

        return detailsFragment;
    }

    public void populateFields() {
        if (aAllergy != null) {
            EditText nameEditTextFieldAllergy = (EditText) getActivity().findViewById(R.id.edit_text_name_allergy);
            CodeableConceptDt codeableConceptAllergy = (CodeableConceptDt) aAllergy.getReaction();
            nameEditTextFieldAllergy.setText(codeableConceptAllergy.getText());

            EditText detailsEditTextFieldAllergy = (EditText) getActivity().findViewById(R.id.edit_text_details_allergy);
            CodeableConceptDt
        }
    }



    public void addAllergyToDatabase(Context context){

    }

}
