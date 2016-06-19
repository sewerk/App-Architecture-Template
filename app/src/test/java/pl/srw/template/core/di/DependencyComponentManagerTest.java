package pl.srw.template.core.di;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;

import pl.srw.template.core.di.component.ActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentInActivityScopeComponent;
import pl.srw.template.core.di.component.FragmentScopeComponent;
import pl.srw.template.core.presenter.BasePresenter;
import pl.srw.template.core.view.BaseActivity;
import pl.srw.template.di.component.ApplicationComponent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class DependencyComponentManagerTest {

    @InjectMocks
    private DependencyComponentManager sut;

    @Mock private ApplicationComponent applicationComponent;
    @Mock private BaseActivity activity;
    @Mock private ActivityScopedFragment activityScopedFragment;
    @Mock private OwnScopeFragment fragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(activity.prepareComponent()).thenReturn(mock(ActivityScopeComponent.class), mock(ActivityScopeComponent.class));
        when(activityScopedFragment.getBaseActivity()).thenReturn(activity);
        when(fragment.prepareComponent()).thenReturn(mock(FragmentScopeComponent.class), mock(FragmentScopeComponent.class));
    }

    @Test
    public void activityComponentIsTheSameAtSecondTime() throws Exception {
        // WHEN
        final ActivityScopeComponent firstTime = sut.getComponentFor(activity);
        final ActivityScopeComponent secondTime = sut.getComponentFor(activity);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsDifferentIfItWasReleased() throws Exception {
        // GIVEN
        final ActivityScopeComponent firstTime = sut.getComponentFor(activity);

        // WHEN
        sut.releaseComponentFor(activity);

        // THEN
        final ActivityScopeComponent secondTime = sut.getComponentFor(activity);
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void activityComponentIsDifferentForDifferentInstance() throws Exception {
        // GIVEN
        final BaseActivity secondActivity = mock(BaseActivity.class);
        when(secondActivity.prepareComponent()).thenReturn(mock(ActivityScopeComponent.class));

        // WHEN
        final ActivityScopeComponent first = sut.getComponentFor(activity);
        final ActivityScopeComponent second = sut.getComponentFor(secondActivity);

        // THEN
        assertNotEquals(first, second);
    }

    @Test
    public void callsPresentersFinishOnActivityComponentRelease() throws Exception {
        // GIVEN
        final ActivityScopeComponent component = sut.getComponentFor(activity);
        final BasePresenter presenter = mock(BasePresenter.class);
        when(component.getPresenters()).thenReturn(Sets.newSet(presenter));

        // WHEN
        sut.releaseComponentFor(activity);

        // THEN
        verify(presenter).onFinish();
    }

    @Test
    public void activityComponentIsSameForFragmentInActivityScope() throws Exception {
        // GIVEN
        when(activity.prepareComponent())
                .thenReturn(mock(ActivityScopeComponent.class, withSettings().extraInterfaces(FragmentInActivityScopeComponent.class)));
        // WHEN
        final ActivityScopeComponent activityComponent = sut.getComponentFor(activity);
        final FragmentInActivityScopeComponent fragmentComponent = sut.getComponentFor(activityScopedFragment);

        // THEN
        assertEquals(activityComponent, fragmentComponent);
    }

    @Test
    public void fragmentComponentIsTheSameAtSecondTime() throws Exception {
        // WHEN
        final FragmentScopeComponent firstTime = sut.getComponentFor(fragment);
        final FragmentScopeComponent secondTime = sut.getComponentFor(fragment);

        // THEN
        assertEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsDifferentIfItWasReleased() throws Exception {
        // GIVEN
        final FragmentScopeComponent firstTime = sut.getComponentFor(fragment);
        when(firstTime.getPresenter()).thenReturn(mock(BasePresenter.class));

        // WHEN
        sut.releaseComponentFor(fragment);

        // THEN
        final FragmentScopeComponent secondTime = sut.getComponentFor(fragment);
        assertNotEquals(firstTime, secondTime);
    }

    @Test
    public void fragmentComponentIsDifferentForDifferentInstance() throws Exception {
        // GIVEN
        final OwnScopeFragment secondFragment = mock(OwnScopeFragment.class);
        when(secondFragment.prepareComponent()).thenReturn(mock(FragmentScopeComponent.class));

        // WHEN
        final FragmentScopeComponent first = sut.getComponentFor(fragment);
        final FragmentScopeComponent second = sut.getComponentFor(secondFragment);

        // THEN
        assertNotEquals(first, second);
    }

    @Test
    public void callsPresenterFinishOnFragmentComponentRelease() throws Exception {
        // GIVEN
        final FragmentScopeComponent component = sut.getComponentFor(fragment);
        final BasePresenter presenter = mock(BasePresenter.class);
        when(component.getPresenter()).thenReturn(presenter);

        // WHEN
        sut.releaseComponentFor(fragment);

        // THEN
        verify(presenter).onFinish();
    }
}