package pl.srw.mfvp.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MvpPresenterTest {

    private TestMvpPresenter sut;

    @Mock private TestView view;

    @Mock private MvpPresenter.UIChange<TestView> uiChange;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sut = new TestMvpPresenter();
    }

    @Test
    public void viewCanRebindAfterUnbind() throws Exception {
        // GIVEN
        sut.bind(view);
        sut.unbind();

        // WHEN
        sut.bind(view);

        // THEN
        // no exception is thrown
    }

    @Test(expected = RuntimeException.class)
    public void concurrentViewBindThrowException() throws Exception {
        // WHEN
        sut.bind(view);
        sut.bind(view);
    }

    @Test
    public void firstBindIsCallsOnFirstBindOnly() throws Exception {
        // GIVEN
        sut = spy(sut);

        // WHEN
        sut.bind(view);

        // THEN
        verify(sut).onFirstBind();
        verify(sut, never()).onNewViewRestoreState();
    }

    @Test
    public void secondBindCallsOnNewViewRestoreState() throws Exception {
        // GIVEN
        sut = spy(sut);

        // WHEN
        sut.bind(view);
        sut.unbind();
        sut.bind(view);

        // THEN
        verify(sut).onNewViewRestoreState();
    }

    @Test
    public void thirdBindCallsOnNewViewRestoreState() throws Exception {
        // GIVEN
        sut = spy(sut);

        // WHEN
        sut.bind(view);
        sut.unbind();
        sut.bind(view);
        sut.unbind();
        sut.bind(view);

        // THEN
        verify(sut, times(2)).onNewViewRestoreState();
    }

    @Test
    public void presentUIChangeIsExecutedImmediatelyWhenViewIsBind() throws Exception {
        // GIVEN
        sut.bind(view);

        // WHEN
        sut.present(uiChange);

        // THEN
        verify(uiChange).change(view);
    }

    @Test
    public void presentUIChangeIsNotExecutedWhenViewIsUnbind() throws Exception {
        // GIVEN
        sut.unbind();

        // WHEN
        sut.present(uiChange);

        // THEN
        verify(uiChange, never()).change(view);
    }

    @Test
    public void bindViewExecutesLatestUIChangeIfWasNotExecuted() throws Exception {
        // GIVEN
        sut.present(uiChange);

        // WHEN
        sut.bind(view);

        // THEN
        verify(uiChange).change(view);
    }

    @Test
    public void bindViewNotExecuteLatestUIChangeIfWasExecuted() throws Exception {
        // GIVEN
        sut.present(uiChange);
        sut.bind(view);
        sut.unbind();

        // WHEN
        sut.bind(view);

        // THEN
        verify(uiChange).change(view);
    }

    private class TestMvpPresenter extends MvpPresenter<TestView> {
    }

    private class TestView {
    }
}