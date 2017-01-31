package pl.srw.mfvp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class DependencyComponentManagerTest {

    @InjectMocks
    private DependencyComponentManager sut;

    @Mock MvpActivity activity;
    @Mock MvpActivity activity2;
    @Mock(extraInterfaces = MvpActivityScopedFragment.class) MvpFragment activityScopedFragment;
    @Mock(extraInterfaces = MvpFragmentScopedFragment.class) MvpFragment fragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(activity.prepareComponent()).thenReturn(mock(MvpComponent.class), mock(MvpComponent.class));
        when(activity2.prepareComponent()).thenReturn(mock(MvpComponent.class), mock(MvpComponent.class));
        when(fragment.getMvpActivity()).thenReturn(activity);
        when(activityScopedFragment.getMvpActivity()).thenReturn(activity);
        when(((MvpFragmentScopedFragment) fragment).prepareComponent(any(MvpComponent.class))).thenReturn(mock(MvpComponent.class), mock(MvpComponent.class));
    }

    @Test
    public void activityComponentIsTheSameAtSecondTime() throws Exception {
        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        final MvpComponent secondTime = sut.getComponentFor(activity);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsTheSameForDifferentInstances() throws Exception {
        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        final MvpComponent secondTime = sut.getComponentFor(activity2);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsDifferentForDifferentTypes() throws Exception {
        // GIVEN
        MvpActivity secondActivity = new MvpActivityTest.TestMvpActivity();

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        final MvpComponent secondTime = sut.getComponentFor(secondActivity);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsTheSameIfItWasReleasedWithoutFinishing() throws Exception {
        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, false);
        final MvpComponent secondTime = sut.getComponentFor(activity2);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsDifferentIfItWasReleasedWithFinishing() throws Exception {
        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, true);
        final MvpComponent secondTime = sut.getComponentFor(activity2);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsTheSameIfItWasReleasedLessTimesThenGet() throws Exception {
        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activity);
        final MvpComponent secondTime = sut.getComponentFor(activity2);
        sut.releaseComponentFor(activity, true);
        final MvpComponent thirdTime = sut.getComponentFor(mock(MvpActivity.class));

        // THEN
        assertEquals(firstTime, thirdTime);
    }

    @Test
    public void activityComponentIsDifferentIfItWasReleasedSameTimesAsGet() throws Exception {
        // WHEN
        MvpComponent firstTime = sut.getComponentFor(activity);
        MvpComponent secondTime = sut.getComponentFor(activity2);
        sut.releaseComponentFor(activity, true);
        sut.releaseComponentFor(activity2, true);
        MvpComponent thirdTime = sut.getComponentFor(activity);

        // THEN
        assertNotEquals(firstTime, thirdTime);
    }

    @Test
    public void fragmentComponentIsTheSameAtSecondTime() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(fragment);
        final MvpComponent secondTime = sut.getComponentFor(fragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsTheSameForDifferentInstances() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        final MvpFragment secondFragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpFragmentScopedFragment.class));
        when(secondFragment.getMvpActivity()).thenReturn(activity);
        when(((MvpFragmentScopedFragment) secondFragment).prepareComponent(any(MvpComponent.class))).thenReturn(mock(MvpComponent.class));

        // WHEN
        final MvpComponent first = sut.getComponentFor(fragment);
        final MvpComponent second = sut.getComponentFor(secondFragment);

        // THEN
        assertEquals(first, second);
    }

    @Test
    public void fragmentComponentIsDifferentForDifferentTypesWithinSameActivity() throws Exception {
        // GIVEN
        MvpFragment secondFragment = new MvpFragmentTest.TestFragmentScopeMvpFragment();
        sut.getComponentFor(secondFragment.getMvpActivity());
        when(fragment.getMvpActivity()).thenReturn(secondFragment.getMvpActivity());

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(fragment);
        final MvpComponent secondTime = sut.getComponentFor(secondFragment);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsTheSameIfItWasReleasedWithoutFinishing() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(fragment);
        sut.releaseComponentFor(fragment, false);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(fragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsDifferentIfItWasReleasedWithFinishing() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(fragment);
        sut.releaseComponentFor(fragment, true);
        final MvpComponent secondTime = sut.getComponentFor(fragment);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsTheSameIfItWasReleasedLessTimesThenGet() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(fragment);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(fragment);
        sut.releaseComponentFor(fragment, true);
        MvpComponent<MvpFragment> thirdTime = sut.getComponentFor(fragment);

        // THEN
        assertEquals(firstTime, thirdTime);
    }

    @Test
    public void fragmentComponentIsDifferentIfItWasReleasedSameTimesAsGet() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(fragment);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(fragment);
        sut.releaseComponentFor(fragment, true);
        sut.releaseComponentFor(fragment, true);
        MvpComponent<MvpFragment> thirdTime = sut.getComponentFor(fragment);

        // THEN
        assertNotEquals(firstTime, thirdTime);
    }

    @Test(expected = IllegalStateException.class)
    public void getComponentForFragment_whenActivityWasNotComponentCached_throwsException() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class);
        when(fragment.getMvpActivity()).thenReturn(activity);

        // WHEN
        sut.getComponentFor(fragment);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameAtSecondTime() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activityScopedFragment);
        final MvpComponent secondTime = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameForDifferentInstancesWithinSameActivity() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        final MvpFragment secondFragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpActivityScopedFragment.class));
        when(secondFragment.getMvpActivity()).thenReturn(activity);

        // WHEN
        final MvpComponent first = sut.getComponentFor(activityScopedFragment);
        final MvpComponent second = sut.getComponentFor(secondFragment);

        // THEN
        assertEquals(first, second);
    }

    @Test
    public void activityScopedFragmentComponentIsDifferentForDifferentActivityTypes() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        MvpFragment secondFragment = new MvpFragmentTest.TestActivityScopedMvpFragment();
        sut.getComponentFor(secondFragment.getMvpActivity());

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activityScopedFragment);
        final MvpComponent secondTime = sut.getComponentFor(secondFragment);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameIfItWasReleasedWithoutFinishing() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(activityScopedFragment);
        sut.releaseComponentFor(activityScopedFragment, false);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameIfItWasReleasedWithFinishing() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        final MvpComponent firstTime = sut.getComponentFor(activityScopedFragment);
        sut.releaseComponentFor(activityScopedFragment, true);
        final MvpComponent secondTime = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameIfItWasReleasedLessTimesThenGet() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(activityScopedFragment);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(activityScopedFragment);
        sut.releaseComponentFor(activityScopedFragment, true);
        MvpComponent<MvpFragment> thirdTime = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(firstTime, thirdTime);
    }

    @Test
    public void activityScopedFragmentComponentIsTheSameIfItWasReleasedSameTimesAsGet() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        MvpComponent<MvpFragment> firstTime = sut.getComponentFor(activityScopedFragment);
        MvpComponent<MvpFragment> secondTime = sut.getComponentFor(activityScopedFragment);
        sut.releaseComponentFor(activityScopedFragment, true);
        sut.releaseComponentFor(activityScopedFragment, true);
        MvpComponent<MvpFragment> thirdTime = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(firstTime, thirdTime);
    }

    @Test
    public void releaseComponent_withLastInstanceAndWithFinishing_returnsTrue() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        boolean result = sut.releaseComponentFor(activity, true);

        // THEN
        assertTrue(result);
    }

    @Test
    public void releaseComponent_withLastInstanceAndWithoutFinishing_returnFalse() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);

        // WHEN
        boolean result = sut.releaseComponentFor(activity, false);

        // THEN
        assertFalse(result);
    }

    @Test
    public void releaseComponent_withNotLastInstanceAndWithFinishing_returnFalse() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        sut.getComponentFor(activity2);

        // WHEN
        boolean result = sut.releaseComponentFor(activity, true);

        // THEN
        assertFalse(result);
    }

    @Test
    public void releaseComponent_forActivityScopedFragment_withFinishing_returnsTrue() throws Exception {
        // WHEN
        boolean result = sut.releaseComponentFor(activityScopedFragment, true);

        // THEN
        assertTrue(result);
    }

    @Test
    public void releaseComponent_forActivityScopedFragment_withoutFinishing_returnFalse() throws Exception {
        // WHEN
        boolean result = sut.releaseComponentFor(activityScopedFragment, false);

        // THEN
        assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void getComponentForFragmentWithoutScopeInterfaceThrowsException() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        MvpFragment fragment = mock(MvpFragment.class);
        when(fragment.getMvpActivity()).thenReturn(activity);

        // WHEN
        sut.getComponentFor(fragment);
    }

    @Test(expected = IllegalStateException.class)
    public void releaseComponent_whichDoesNotHaveInstanceCached_throwsException() throws Exception {
        // WHEN
        sut.releaseComponentFor(activity, true);
    }

    @Test(expected = IllegalStateException.class)
    public void releaseComponent_whichWasAlreadyReleased_withFinishing_throwsException() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, true);

        // WHEN
        sut.releaseComponentFor(activity, true);
    }

    @Test(expected = IllegalStateException.class)
    public void releaseComponent_whichWasAlreadyReleased_withoutFinishing_throwsException() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, false);

        // WHEN
        sut.releaseComponentFor(activity, false);
    }

    @Test
    public void releaseComponent_withFinishing_whichWasAlreadyReleased_withoutFinishing_doesNotThrowException() throws Exception {
        // GIVEN
        sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, false);

        // WHEN
        sut.getComponentFor(activity);
        sut.releaseComponentFor(activity, true);

        // THEN
        // no exception is thrown
    }
}