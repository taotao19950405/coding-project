package orionhealth.app.activities.adaptors;

import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;

/**
 * Created by archanakhanal on 18/7/2016.
 */
public class MySimpleListAdapter {
    private Context aContext;
    private Cursor aCursor;

    public MySimpleListAdapter(Context context){
        this.aContext = context;
    }

    public void OnEditButtonClicked(int allergyLocalId){
    }

    public int getGroupCount() {
        return aCursor.getCount();
    }




}

