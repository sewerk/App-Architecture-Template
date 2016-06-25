package pl.srw.template.core.di;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.template.core.di.component.MvpActivityScopeComponent;
import pl.srw.template.core.di.component.MvpFragmentInActivityScopeComponent;
import pl.srw.template.core.di.component.MvpFragmentScopeComponent;
import pl.srw.template.core.view.activity.MvpActivity;
import pl.srw.template.core.view.fragment.MvpActivityScopedFragment;
import pl.srw.template.core.view.fragment.MvpFragmentScopedFragment;
import pl.srw.template.di.component.ApplicationComponent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class DependencyComponentManagerTest {

    @InjectMocks
    private DependencyComponentManager sut;

    @Mock private ApplicationComponent applicationComponent;
    @Mock private MvpActivity activity;
    @Mock private MvpActivityScopedFragment activityScopedFragment;
    @Mock private MvpFragmentScopedFragment fragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(activity.prepareComponent()).thenReturn(mock(MvpActivityScopeComponent.class), mock(MvpActivityScopeComponent.class));
        when(activityScopedFragment.getBaseActivity()).thenReturn(activity);
        when(fragment.prepareComponent()).thenReturn(mock(MvpFragmentScopeComponent.class), mock(MvpFragmentScopeComponent.class));
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
    public void activityComponentIsSameForFragmentInActivityScope() throws Exception {
        // GIVEN
        when(activity.prepareComponent())
                .thenReturn(mock(MvpActivityScopeComponent.class, withSettings().extraInterfaces(MvpFragmentInActivityScopeComponent.class)));
        // WHEN
        final MvpActivityScopeComponent activityComponent = sut.getComponentFor(activity);
        final MvpFragmentInActivityScopeComponent fragmentComponent = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(activityComponent, fragmentComponent);
    }

    @Test
    public void fragmentComponentIsTheSameAtSecondTime() throws Exception {
        // WHEN
        final MvpFragmentScopeComponent firstTime = sut.getComponentFor(fragment);
        final MvpFragmentScopeComponent secondTime = sut.getComponentFor(fragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsDifferentIfItWasReleased() throws Exception {
        // GIVEN
        final MvpFragmentScopeComponent firstTime = sut.getComponentFor(fragment);

        // WHEN
        sut.releaseComponentFor(fragment);

        // THEN
        final MvpFragmentScopeComponent secondTime = sut.getComponentFor(fragment);
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsSameForDifferentInstance() throws Exception {
        // GIVEN
        final MvpFragmentScopedFragment secondFragment = mock(MvpFragmentScopedFragment.class);
        when(secondFragment.prepareComponent()).thenReturn(mock(MvpFragmentScopeComponent.class));

        // WHEN
        final MvpFragmentScopeComponent first = sut.getComponentFor(fragment);
        final MvpFragmentScopeComponent second = sut.getComponentFor(secondFragment);

        // THEN
        assertEquals(first, second);
    }
}