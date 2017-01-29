package pl.srw.mfvp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;

import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class MvpActivityTest {

    @Spy
    private TestMvpActivity sut;

    @Mock
    private FragmentManager fragmentManagerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final FragmentTransaction fragmentTransactionMock = mock(FragmentTransaction.class);
        when(fragmentTransactionMock.replace(anyInt(), any(Fragment.class), anyString())).thenReturn(fragmentTransactionMock);
        when(fragmentTransactionMock.addToBackStack(anyString())).thenReturn(fragmentTransactionMock);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
        doReturn(fragmentManagerMock).when(sut).getSupportFragmentManager();
    }

    @Test
    public void onDestroy_whenActivityIsFinishing_fragmentScopedFragmentEndOfScope() throws Exception {
        // GIVEN
        doReturn(true).when(sut).isFinishing();
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpFragmentScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));

        // WHEN
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(fragment).endOfScope();
    }

    @Test
    public void onDestroy_whenActivityIsFinishing_activityScopedFragmentEndOfScope() throws Exception {
        // GIVEN
        doReturn(true).when(sut).isFinishing();
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpActivityScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));

        // WHEN
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(fragment).endOfScope();
    }

    @Test
    public void changeFragment_fromFragmentScopedFragment_fragmentEndOfScope() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpFragmentScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));

        // WHEN
        sut.changeFragment(0, mock(Fragment.class), null);

        // THEN
        verify(fragment).endOfScope();
    }

    @Test
    public void changeFragment_fromActivityScopedFragment_fragmentIsNotEndOfScope() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpActivityScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));

        // WHEN
        sut.changeFragment(0, mock(Fragment.class), null);

        // THEN
        verify(fragment, never()).endOfScope();
    }

    @Test
    public void changeFragmentWithStack_fromFragmentScopedFragment_fragmentEndOfScope() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpFragmentScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));

        // WHEN
        sut.changeFragmentWithStack(0, mock(Fragment.class), null);

        // THEN
        verify(fragment, never()).endOfScope();
    }

    @Test
    public void onBackPressed_onFragmentScopedFragment_fragmentEndOfScope() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpFragmentScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(1);

        // WHEN
        try {
            sut.onBackPressed();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onBackPressed
        }

        // THEN
        verify(fragment).endOfScope();
    }

    @Test
    public void onBackPressed_onActivityScopedFragment_fragmentIsNotEndOfScope() throws Exception {
        // GIVEN
        MvpFragment fragment = mock(MvpFragment.class, withSettings().extraInterfaces(MvpActivityScopedFragment.class));
        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList((Fragment)fragment));
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(1);

        // WHEN
        try {
            sut.onBackPressed();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onBackPressed
        }

        // THEN
        verify(fragment, never()).endOfScope();
    }

    @Test
    @Ignore // TODO
    public void onCreate_injectsDependencies() throws Exception {
        // GIVEN
        // set mocked dependencies instance by Whitebox
        // WHEN
        // THEN
    }

    @Test
    @Ignore // TODO
    public void onStart_bindPresenter() throws Exception {
        // GIVEN
        // WHEN
        // THEN
    }

    @Test
    @Ignore // TODO
    public void onStop_unbindPresenter() throws Exception {
        // GIVEN
        // WHEN
        // THEN
    }

    @Test
    @Ignore // TODO
    public void onDestroy_resetDependencies() throws Exception {
        // GIVEN
        // WHEN
        // THEN
    }

    @Test
    @Ignore // TODO
    public void onDestroy_finishPresenter() throws Exception {
        // GIVEN
        // WHEN
        // THEN
    }

    public static class TestMvpActivity extends MvpActivity {

        public TestMvpActivity() {
        }

        @Override
        protected MvpPresenter getPresenter() {
            return mock(MvpPresenter.class);
        }

        @Override
        public MvpComponent prepareComponent() {
            return mock(MvpComponent.class);
        }

        @Override
        protected int getContentLayoutId() {
            return 0;
        }
    }
}