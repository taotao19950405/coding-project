//       Description:
//		 @author:  Bill

package orionhealth.app.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;
import orionhealth.app.R;
import orionhealth.app.medicationDatabase.*;
import orionhealth.app.medicationDatabase.DatabaseContract.*;

public class MyMedicationActivity extends AppCompatActivity {
	public final static String SELECTED_MED_ID = "orionhealth.app.SELECTED_MED_ID";

	private String[] fromColumns = {MedTableInfo.COLUMN_NAME_NAME, MedTableInfo.COLUMN_NAME_DOSAGE};
	private int[] toViews = {R.id.list_display_name, R.id.list_display_dosage};

	private ListView mDrawerList;
	private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_medication);

		mDrawerList = (ListView)findViewById(R.id.navigation_drawer_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

		Cursor cursor = MedTableOperations.getAllRows(this);
		SimpleCursorAdapter adapter =
		  	new SimpleCursorAdapter(this, R.layout.list_medication_layout, cursor, fromColumns, toViews, 0);
		ListView listView = (ListView) findViewById(R.id.my_medication_list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				Intent intent = new Intent(getBaseContext(), EditMedicationActivity.class);
				intent.putExtra(SELECTED_MED_ID, id);
				startActivity(intent);
			}
		});
	}

    /** Populates Navigation Menu with Names
     * Sets a click listener for an action to be specified
     * if an item in the menu is clicked*/
    private void addDrawerItems() {
		String[] navDrawerArray = { "My Medication", "My Allergies", "My Symptoms", "My Calendar", "Settings"};
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawerArray);
		mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyMedicationActivity.this, "This Item", Toast.LENGTH_SHORT).show();
            }
        });
	}

    /** Methods to be called when drawer is toggled
     * between open and closed states */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_my_medication, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as a parent activity is specified in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.button_add) {
			Intent intent = new Intent(this, AddMedicationActivity.class);
			startActivity(intent);
			return true;
		}

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

		return super.onOptionsItemSelected(item);
	}
}
