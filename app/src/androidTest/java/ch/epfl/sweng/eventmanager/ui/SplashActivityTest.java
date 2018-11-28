package ch.epfl.sweng.eventmanager.ui;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

public class SplashActivityTest {

    @Rule
    public final ActivityTestRule<SplashActivity> mActivityRule =
            new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void testLaunch() {
    }
}