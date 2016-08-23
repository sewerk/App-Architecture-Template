package pl.srw.mfvp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.component.MvpFragmentScopeComponent;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MvpFragmentTest {

    @Spy
    private TestMvpFragment sut;

    @Mock
    private FragmentManager mFragmentManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final FragmentTransaction fragmentTransactionMock = mock(FragmentTransaction.class);
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransactionMock);
//        doReturn(fragmentManagerMock).when(sut).getFragmentManager();
    }

    @Test
    public void dismiss_fragmentWillNotEndOfScopeOnDestroy() throws Exception {
        // WHEN
        try {
            sut.dismiss();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.dismiss
        }
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(sut, never()).endOfScope();
    }

    @Test
    public void dismiss_whenFragmentScopeFragment_fragmentIsEndOfScopeOnDestroy() throws Exception {
        // GIVEN
        TestMvpFramgmentScopeFragment sut = spy(new TestMvpFramgmentScopeFragment());

        // WHEN
        try {
            sut.dismiss();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.dismiss
        }
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(sut).endOfScope();
    }

    @Test
    public void onCancel_fragmentWillNotEndOfScopeOnDestroy() throws Exception {
        // WHEN
        try {
            sut.onCancel(null);
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(sut, never()).endOfScope();
    }

    @Test
    public void onCancel_whenFragmentScopeFragment_fragmentIsEndOfScopeOnDestroy() throws Exception {
        // GIVEN
        TestMvpFramgmentScopeFragment sut = spy(new TestMvpFramgmentScopeFragment());

        // WHEN
        try {
            sut.onCancel(null);
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }
        try {
            sut.onDestroy();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onDestroy
        }

        // THEN
        verify(sut).endOfScope();
    }

    public static class TestMvpFragment extends MvpFragment {
    }

    public static class TestMvpFramgmentScopeFragment extends MvpFragment implements MvpFragmentScopedFragment {
        @Override
        public MvpFragmentScopeComponent getFragmentComponent(MvpActivityScopeComponent activityComponent) {
            return null;
        }
    }
}