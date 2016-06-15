package orionhealth.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import orionhealth.app.activities.main.MyMedicationActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by bill on 13/06/16.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TrialTest {

	private String mStringToBetyped;

	@Rule
	public ActivityTestRule<MyMedicationActivity> mActivityRule = new ActivityTestRule<>(
	  MyMedicationActivity.class);

	@Before
	public void initValidString() {
		// Specify a valid string.
		mStringToBetyped = "Espresso";
	}

	@Test
	public void changeText_sameActivity() {
		// Type text and then press the button.
		onView(withId(R.id.button_add))
		  .perform(click());

		// Check fragment is displayed
		onView(withId(R.id.fragment_medication_details))
		  .check(matches(isDisplayed()));

		// Check that all text fields are empty.
		onView(withId(R.id.edit_text_name))
		  .check(matches(withText("")));

		onView(withId(R.id.edit_text_dosage))
		  .check(matches(withText("")));

		onView(withId(R.id.edit_text_effectiveStart))
		  .check(matches(withText("")));

		onView(withId(R.id.edit_text_effectiveEnd))
		  .check(matches(withText("")));

		onView(withId(R.id.edit_text_instructions))
		  .check(matches(withText("")));

		onView(withId(R.id.edit_text_reasonForUse))
		  .check(matches(withText("")));

		onView(withId(R.id.button_add))
		  .perform(click());

		// Check that list of medication is displayed
		onView(withId(R.id.fragment_medication_details))
		  .check(doesNotExist());
	}
}


