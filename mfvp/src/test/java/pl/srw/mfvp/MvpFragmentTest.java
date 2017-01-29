package pl.srw.mfvp;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.util.reflection.Whitebox;

import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.view.fragment.MvpActivityScopedFragment;
import pl.srw.mfvp.view.fragment.MvpFragmentScopedFragment;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MvpFragmentTest {

    @Spy
    private TestActivityScopedMvpFragment sut;

    @Spy
    private TestFragmentScopeMvpFragment sutFragmentScoped;

    @Mock
    private FragmentManager mFragmentManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        final FragmentTransaction fragmentTransactionMock = mock(FragmentTransaction.class);
        when(mFragmentManager.beginTransaction()).thenReturn(fragmentTransactionMock);
    }

    @Test
    public void dismiss_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sut.dismiss();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.dismiss
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sut, "isFinishing")));
    }

    @Test
    public void dismiss_whenFragmentScopeFragment_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sutFragmentScoped.dismiss();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.dismiss
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sutFragmentScoped, "isFinishing")));
    }

    @Test
    public void onCancel_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sut.onCancel(null);
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sut, "isFinishing")));
    }

    @Test
    public void onCancel_whenFragmentScopeFragment_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sutFragmentScoped.onCancel(null);
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sutFragmentScoped, "isFinishing")));
    }

    @Test
    public void endOfScope_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sut.endOfScope();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sut, "isFinishing")));
    }

    @Test
    public void endOfScope_whenFragmentScopeFragment_setsIsFinishing() throws Exception {
        // WHEN
        try {
            sutFragmentScoped.endOfScope();
        } catch (RuntimeException e) {
            // cannot mock FragmentActivity.onCancel
        }

        // THEN
        assertTrue(((Boolean) Whitebox.getInternalState(sutFragmentScoped, "isFinishing")));
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

    public static class TestActivityScopedMvpFragment extends MvpFragment implements MvpActivityScopedFragment<MvpComponent> {
        @Override
        protected MvpPresenter getPresenter() {
            return mock(MvpPresenter.class);
        }

        @Override
        MvpActivity getMvpActivity() {
            return new MvpActivityTest.TestMvpActivity();
        }

        @Override
        public void injectDependencies(MvpComponent activityComponent) {
        }
    }

    public static class TestFragmentScopeMvpFragment extends MvpFragment implements MvpFragmentScopedFragment {
        @Override
        protected MvpPresenter getPresenter() {
            return mock(MvpPresenter.class);
        }

        @Override
        public MvpComponent prepareComponent(MvpComponent activityComponent) {
            return mock(MvpComponent.class);
        }

        @Override
        MvpActivity getMvpActivity() {
            return new MvpActivityTest.TestMvpActivity();
        }}
}