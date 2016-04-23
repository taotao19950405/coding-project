//       Description:
//		 @author:  Bill
// 		 @Reviewer: 

package orionhealth.app.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import orionhealth.app.R;
import orionhealth.app.fragments.Fragments.*;
import orionhealth.app.fragments.ListFragments.*;

public class MyMedicationActivity extends AppCompatActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	TabLayout tabs;
	String[] tabsTitles = {"My Medication", "Today", "My Allergies", "Notifications", "Calendar"};

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

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		tabs = (TabLayout) findViewById(R.id.sliding_tabs);
		tabs.setupWithViewPager(mViewPager);

		tabs.getTabAt(0).setIcon(R.mipmap.ic_local_hospital_white_24dp);
		tabs.getTabAt(1).setIcon(R.mipmap.ic_wb_sunny_white_24dp);
		tabs.getTabAt(2).setIcon(R.mipmap.ic_warning_white_24dp);
		tabs.getTabAt(3).setIcon(R.mipmap.ic_notifications_none_white_24dp);
		tabs.getTabAt(4).setIcon(R.mipmap.ic_date_range_white_24dp);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {

			}

			@Override
			public void onPageSelected(int i) {
				getSupportActionBar().setTitle(tabsTitles[i]);
			}

			@Override
			public void onPageScrollStateChanged(int i) {

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


	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0){
				return new MedicationListFragment();
			}
			return UnderConstructionFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
	}



}
