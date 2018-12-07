package ch.epfl.sweng.eventmanager.ui.event.interaction;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.DatePicker;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import ch.epfl.sweng.eventmanager.R;
import ch.epfl.sweng.eventmanager.repository.data.Event;
import ch.epfl.sweng.eventmanager.test.EventTestRule;
import ch.epfl.sweng.eventmanager.test.TestApplication;
import ch.epfl.sweng.eventmanager.test.repository.MockEventsRepository;
import ch.epfl.sweng.eventmanager.test.users.DummyInMemorySession;
import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;
import ch.epfl.sweng.eventmanager.users.Session;
import org.hamcrest.Matchers;
import org.junit.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

public class EventCreateActivityTest {
    @Rule
    public final EventTestRule<EventCreateActivity> mActivityRule = new EventTestRule<>(EventCreateActivity.class, -1); // Creation


    @Inject
    protected MockEventsRepository repository;
    @Inject
    Session session;

    public EventCreateActivityTest() {
        TestApplication.component.inject(this);
    }

    @Before
    public void setup() {
        session.login(DummyInMemorySession.DUMMY_EMAIL, DummyInMemorySession.DUMMY_PASSWORD, null, null);

        Intents.init();
        intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(getCroppedImageResult());
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testCreateEvent() {
        onView(withId(R.id.create_form_name)).perform(typeText("Event Test 1"), closeSoftKeyboard());
        onView(withId(R.id.create_form_email)).perform(typeText("you@test.com"), closeSoftKeyboard());
        this.setDate(R.id.create_form_begin_date, 2018, 12, 1);
        this.setDate(R.id.create_form_end_date, 2018, 12, 4);
        onView(withId(R.id.create_form_description)).perform(typeText("Event Test 1 description"), closeSoftKeyboard());
        onView(withId(R.id.create_form_upload_image)).perform(scrollTo(),click());
        onView(withId(R.id.create_form_send_button)).perform(scrollTo(),click());

        Intents.intended(Matchers.allOf(IntentMatchers.hasComponent(EventAdministrationActivity.class.getName()), IntentMatchers.hasExtraWithKey(EventPickingActivity.SELECTED_EVENT_ID)));

        // Find event
        boolean found = false;
        for (Event e : repository.getEvents().getValue()) {
            if (e.getName().equalsIgnoreCase("Event Test 1")) {
                found = true;

                Assert.assertEquals("you@test.com", e.getOrganizerEmail());
                Assert.assertEquals("Event Test 1 description", e.getDescription());
                Assert.assertNotNull(e.getBeginDate());
                Assert.assertNotNull(e.getEndDate());
                Assert.assertNull(e.getTwitterName());
            }
        }

        Assert.assertTrue("Event was not found in repository after creation", found);
    }

    private void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }

    private Instrumentation.ActivityResult getCroppedImageResult() {
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcels = new ArrayList<>();
        Intent resultData = new Intent();
        Uri src = getPickedImage();
        Parcelable parcelable1 = (Parcelable) src;
        parcels.add(parcelable1);
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, parcels);
        // Create the Intent that will include the bundle.
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
    }

    private Uri getPickedImage() {
        Bitmap bm = BitmapFactory.decodeResource(mActivityRule.getActivity().getResources(), R.drawable.event_default_cover);
        assertTrue(bm != null);
        File dir = mActivityRule.getActivity().getExternalCacheDir();
        File file = null;
        try {
            file = File.createTempFile("event_image","png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }
}