package orionhealth.app.activities.fragments.listFragments;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import orionhealth.app.R;
import orionhealth.app.activities.main.AddAllergyActivity;
import orionhealth.app.data.medicationDatabase.AllergyTableOperations;

/**
 * Created by archanakhanal on 8/7/2016.
 */
public class AllergyListFragment extends ListFragment {
    public final static String SELECTED_ALLERGY_ID = "allergyListFragment.SELECTED_ALLERGY_ID";
    private ListView allergyList;


    public AllergyListFragment() {
    }

    public static AllergyListFragment newInstance() {
        AllergyListFragment allergyFragment = new AllergyListFragment();
        return allergyFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_allergies_list, container, false);
        return view;
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Cursor cursor = AllergyTableOperations.getInstance().getAllRows(getContext());

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);
    }
}
