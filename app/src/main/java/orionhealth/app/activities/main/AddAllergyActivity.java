package orionhealth.app.activities.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import orionhealth.app.R;
import orionhealth.app.activities.fragments.fragments.AllergyDetailsFragment;

/**
 * Created by archanakhanal on 11/7/2016.
 */
public class AddAllergyActivity extends AppCompatActivity {

	public static final String ActivityKey = "Allergy";
    private AllergyDetailsFragment aAllergyDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergy);

        FragmentManager fragmentManager = getFragmentManager();
        aAllergyDetailsFragment =
                (AllergyDetailsFragment) fragmentManager.findFragmentById(R.id.fragment_allergy_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_allergy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void addAllergyToDatabase(View view) {
		try {
			aAllergyDetailsFragment.addAllergyToDatabase(this);
			Intent intentAllergy = new Intent(this, MainActivity.class);
			intentAllergy.putExtra("ACTIVITY", ActivityKey);
			startActivity(intentAllergy);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
