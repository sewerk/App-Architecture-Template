package pl.srw.mfvp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.component.MvpFragmentScopeComponent;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyComponentManagerTest {

    @InjectMocks
    private DependencyComponentManager sut;

    @Mock private MvpActivity activity;
    @Mock private MvpActivityScopedFragment activityScopedFragment;
    @Mock private MvpFragmentScopedFragment fragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(activity.prepareComponent()).thenReturn(mock(MvpActivityScopeComponent.class), mock(MvpActivityScopeComponent.class));
        when(fragment.getFragmentComponent(any(MvpActivityScopeComponent.class)))
                .thenReturn(mock(MvpFragmentScopeComponent.class), mock(MvpFragmentScopeComponent.class));
    }

    @Test
    public void activityComponentIsTheSameAtSecondTime() throws Exception {
        // WHEN
        final MvpActivityScopeComponent firstTime = sut.getComponentFor(activity);
        final MvpActivityScopeComponent secondTime = sut.getComponentFor(activity);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsDifferentIfItWasReleased() throws Exception {
        // GIVEN
        final MvpActivityScopeComponent firstTime = sut.getComponentFor(activity);

        // WHEN
        sut.releaseComponentFor(activity);

        // THEN
        final MvpActivityScopeComponent secondTime = sut.getComponentFor(activity);
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsSameForDifferentInstance() throws Exception {
        // GIVEN
        final MvpActivity secondActivity = mock(MvpActivity.class);
        when(secondActivity.prepareComponent()).thenReturn(mock(MvpActivityScopeComponent.class));

        // WHEN
        final MvpActivityScopeComponent first = sut.getComponentFor(activity);
        final MvpActivityScopeComponent second = sut.getComponentFor(secondActivity);

        // THEN
        assertEquals(first, second);
    }

    @Test
    public void fragmentComponentIsTheSameAtSecondTime() throws Exception {
        // GIVEN
        MvpActivityScopeComponent activityComponent = mock(MvpActivityScopeComponent.class);

        // WHEN
        final MvpFragmentScopeComponent firstTime = sut.getComponentFor(fragment, activityComponent);
        final MvpFragmentScopeComponent secondTime = sut.getComponentFor(fragment, activityComponent);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsDifferentIfItWasReleased() throws Exception {
        // GIVEN
        MvpActivityScopeComponent activityComponent = mock(MvpActivityScopeComponent.class);

        // WHEN
        final MvpFragmentScopeComponent firstTime = sut.getComponentFor(fragment, activityComponent);
        sut.releaseComponentFor(fragment);
        final MvpFragmentScopeComponent secondTime = sut.getComponentFor(fragment, activityComponent);

        // THEN
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsSameForDifferentInstance() throws Exception {
        // GIVEN
        MvpActivityScopeComponent activityComponent = mock(MvpActivityScopeComponent.class);

        final MvpFragmentScopedFragment secondFragment = mock(MvpFragmentScopedFragment.class);
        when(secondFragment.getFragmentComponent(activityComponent)).thenReturn(mock(MvpFragmentScopeComponent.class));

        // WHEN
        final MvpFragmentScopeComponent first = sut.getComponentFor(fragment, activityComponent);
        final MvpFragmentScopeComponent second = sut.getComponentFor(secondFragment, activityComponent);

        // THEN
        assertEquals(first, second);
    }
}