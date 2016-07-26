//       Description:
//		 @author:  Bill
// 		 @Reviewer: 

package orionhealth.app.activities.main;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import orionhealth.app.activities.fragments.fragments.UnderConstructionFragment;
import orionhealth.app.activities.fragments.listFragments.AllergyListFragment;
import orionhealth.app.activities.fragments.listFragments.ConditionListFragment;
import orionhealth.app.activities.fragments.listFragments.MedicationListFragment;
import orionhealth.app.data.medicationDatabase.DatabaseInitializer;

public class MainActivity extends AppCompatActivity {

	private TabbedPagerAdapter mTabbedPagerAdapter;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private String[] mTabsTitles = {"My Medication", "Today", "My Allergies", "Symptoms", "Calendar"};
	private int mNumOfTabs = mTabsTitles.length;

	private ListView mDrawerList;
	private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
	private String[] mHamburgerTitles = {"Profile", "Notifications", "Settings"};
	private static String SAVED_SLIDE_POSITION = "SAVED_SLIDE_POSITION";
	public static int CurrentTabNumber = 0;


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_medication);
		DatabaseInitializer.getInstance(this);   // Update Database if needed

//      AllergyTableOperations.getInstance().clearAllergyTable(this);
//		MedTableOperations.getInstance().clearMedTable(this);
//		CondTableOperations.getInstance().clearCondTable(this);

		mDrawerList = (ListView)findViewById(R.id.navigation_drawer_list);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

		int width = getResources().getDisplayMetrics().widthPixels/4 * 3;
		DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mDrawerList.getLayoutParams();
		params.width = width;
		mDrawerList.setLayoutParams(params);

        addDrawerItems();
        setupDrawer();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mTabbedPagerAdapter = new TabbedPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mTabbedPagerAdapter);

		if (savedInstanceState != null) {
			CurrentTabNumber = savedInstanceState.getInt(SAVED_SLIDE_POSITION);
		}
		mViewPager.setCurrentItem(CurrentTabNumber);
		getSupportActionBar().setTitle(mTabsTitles[CurrentTabNumber]);


		mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		mTabLayout.setupWithViewPager(mViewPager);

		setTabIcons();

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i1) {
				return;
			}

			@Override
			public void onPageSelected(int i) {
				getSupportActionBar().setTitle(mTabsTitles[i]);
				CurrentTabNumber = i;
			}

			@Override
			public void onPageScrollStateChanged(int i) {
				return;
			}
		});

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

		// Activate the navigation drawer toggle
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*-------------The following methods were added to support HamburgerMenu-----------*/

    /** Populates Navigation Menu with Names
     * Sets a click listener for an action to be specified
     * if an item in the menu is clicked*/
    private void addDrawerItems() {
		String[] navDrawerArray = mHamburgerTitles;
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawerArray);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Item", Toast.LENGTH_SHORT).show();
            }
        });
	}

    /** Methods to be called when drawer is toggled
     * between open and closed states */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

			private CharSequence titleStore;

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
				titleStore = getSupportActionBar().getTitle();
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(titleStore);
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

	/*--------the following was added to support tabbed navigation--------*/

	public void setTabIcons(){
		mTabLayout.getTabAt(0).setIcon(R.mipmap.ic_local_hospital_white_24dp);
		mTabLayout.getTabAt(1).setIcon(R.mipmap.ic_wb_sunny_white_24dp);
		mTabLayout.getTabAt(2).setIcon(R.mipmap.ic_warning_white_24dp);
		mTabLayout.getTabAt(3).setIcon(R.mipmap.ic_notifications_none_white_24dp);
		mTabLayout.getTabAt(4).setIcon(R.mipmap.ic_date_range_white_24dp);
	}

	public class TabbedPagerAdapter extends FragmentPagerAdapter {

		public TabbedPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position){
				case 0: return MedicationListFragment.newInstance();
				case 2: return AllergyListFragment.newInstance();
				case 3: return ConditionListFragment.newInstance();
				default: return UnderConstructionFragment.newInstance();
			}
		}

		@Override
		public int getCount() {
			return mNumOfTabs;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_SLIDE_POSITION, CurrentTabNumber);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_SLIDE_POSITION));
	}
}
