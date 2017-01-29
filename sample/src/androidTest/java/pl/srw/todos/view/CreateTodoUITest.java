package pl.srw.todos.view;

import android.content.pm.ActivityInfo;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.todos.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateTodoUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void shouldShowElementOnListWhenCreated() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add),
                        withParent(allOf(withId(R.id.fragment),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.add_text), isDisplayed()));
        appCompatEditText.perform(replaceText("abc"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_add), withText("ADD NEW"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("abc"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        2),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("abc")));
    }

    @Test
    public void elementCheckedOnListWhenCheckedDuringCreation() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add),
                        withParent(allOf(withId(R.id.fragment),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.add_text), isDisplayed()));
        appCompatEditText.perform(replaceText("checked"), closeSoftKeyboard());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.add_is_done), isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_add), withText("ADD NEW"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction checkBox = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                2),
                        0),
                        isDisplayed()));
        checkBox.check(matches(isChecked()));

        ViewInteraction textView = onView(
                allOf(withText("checked"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        2),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("checked")));
    }

    @Test
    public void elementPersistCheckedAfterScreenRotation() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.add),
                        withParent(allOf(withId(R.id.fragment),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.add_text), isDisplayed()));
        appCompatEditText.perform(replaceText("rotated"), closeSoftKeyboard());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.add_is_done), isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_add), withText("ADD NEW"), isDisplayed()));
        appCompatButton.perform(click());

        MainActivity activity = mActivityTestRule.getActivity();
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                || activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        ViewInteraction checkBox = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                withId(R.id.list),
                                2),
                        0),
                        isDisplayed()));
        checkBox.check(matches(isChecked()));

        ViewInteraction textView = onView(
                allOf(withText("rotated"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.list),
                                        2),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("rotated")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
