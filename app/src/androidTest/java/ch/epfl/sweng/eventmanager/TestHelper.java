package ch.epfl.sweng.eventmanager;

import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.matcher.BoundedMatcher;

import static org.hamcrest.CoreMatchers.is;

public class TestHelper {
    /**
     * Stolen from StackOverflow question 36329978.
     */
    public static Matcher<View> withToolbarTitle(CharSequence title) {
        return withToolbarTitle(is(title));
    }

    /**
     * Stolen from StackOverflow question 36329978.
     */
    private static Matcher<View> withToolbarTitle(final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    /**
     * Stolen from StackOverflow question 42668617.
     */
    public static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("has no error text: ");
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }
}
