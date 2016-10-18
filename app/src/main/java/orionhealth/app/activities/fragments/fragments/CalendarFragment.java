package orionhealth.app.activities.fragments.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import orionhealth.app.R;

/**
 * Created by bill on 17/10/16.
 */

public class CalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View calendarFragment = inflater.inflate(R.layout.fragment_calendar, container, false);


        return calendarFragment;
    }
}
