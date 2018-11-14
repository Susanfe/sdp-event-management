package ch.epfl.sweng.eventmanager.ui;

import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;

public class SplashActivityTest {

    @Rule
    public final ActivityTestRule<SplashActivity> mActivityRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void testLaunch() {
    }
}