package orionhealth.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import orionhealth.app.R;

/**
 * Created by bill on 23/04/16.
 */
public class Fragments {

	public static class UnderConstructionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static UnderConstructionFragment newInstance(int sectionNumber) {
			UnderConstructionFragment fragment = new UnderConstructionFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public UnderConstructionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_construction, container, false);
			return rootView;
		}
	}

}
