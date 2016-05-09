package orionhealth.app.layouts.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import orionhealth.app.R;
import orionhealth.app.layouts.externalResources.AnimatedExpandableListView;

/**
 * Created by bill on 4/05/16.
 */
public class MyExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
	private Context context;

	public MyExpandableListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getGroupCount() {
		return 3;
	}


	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
		  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_medication_list_item, null);
		return result;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
		  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View result = inflater.inflate(R.layout.fragment_trial, null);
		return result;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
