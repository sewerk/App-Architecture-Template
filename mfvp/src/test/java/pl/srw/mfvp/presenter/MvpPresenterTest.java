package pl.srw.mfvp.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
        sut.unbind(view);

        // WHEN
        sut.bind(view);

        // THEN
        // no exception is thrown
    }

    @Test
    public void nextViewCanBindBeforeFirstUnbind() throws Exception {
        // GIVEN
        sut = spy(sut);
        TestView view1 = view;
        TestView view2 = mock(TestView.class);

        // WHEN
        sut.bind(view1);
        sut.bind(view2);

        // THEN
        // no exception is thrown
    }

    @Test
    public void oldViewUnbindDoesNotUnbindView() throws Exception {
        // GIVEN
        sut = spy(sut);
        TestView view1 = view;
        TestView view2 = mock(TestView.class);

        // WHEN
        sut.bind(view1);
        sut.bind(view2);
        sut.unbind(view1);
        sut.testChangeUI();

        // THEN
        verify(view2).testChangeUI();
    }

    @Test
    public void firstBindCallsOnFirstBindOnly() throws Exception {
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
        sut.unbind(view);
        sut.bind(view);

        // THEN
        InOrder inOrder = inOrder(sut);
        inOrder.verify(sut).onFirstBind();
        inOrder.verify(sut).onNewViewRestoreState();
    }

    @Test
    public void thirdBindCallsOnNewViewRestoreState() throws Exception {
        // GIVEN
        sut = spy(sut);

        // WHEN
        sut.bind(view);
        sut.unbind(view);
        sut.bind(view);
        sut.unbind(view);
        sut.bind(view);

        // THEN
        InOrder inOrder = inOrder(sut);
        inOrder.verify(sut).onFirstBind();
        inOrder.verify(sut, times(2)).onNewViewRestoreState();
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
        sut.unbind(view);

        // WHEN
        sut.present(uiChange);

        // THEN
        verify(uiChange, never()).change(view);
    }

    @Test
    public void presentMultipleUIChangesAfterViewIsBind() throws Exception {
        // GIVEN
        MvpPresenter.UIChange<TestView> uiChange1 = mock(MvpPresenter.UIChange.class);
        MvpPresenter.UIChange<TestView> uiChange2 = mock(MvpPresenter.UIChange.class);
        MvpPresenter.UIChange<TestView> uiChange3 = mock(MvpPresenter.UIChange.class);
        sut.present(uiChange1);
        sut.present(uiChange2);
        sut.present(uiChange3);

        // WHEN
        sut.bind(view);

        // THEN
        InOrder inOrder = inOrder(uiChange1, uiChange2, uiChange3);
        inOrder.verify(uiChange1).change(view);
        inOrder.verify(uiChange2).change(view);
        inOrder.verify(uiChange3).change(view);
    }

    @Test
    public void bindViewDoesNotExecuteAgainLatestUIChangeIfWasExecuted() throws Exception {
        // GIVEN
        sut.present(uiChange);
        sut.bind(view);
        verify(uiChange).change(view);
        sut.unbind(view);

        // WHEN
        sut.bind(view);

        // THEN
        verifyNoMoreInteractions(uiChange);
    }

    private class TestMvpPresenter extends MvpPresenter<TestView> {
        public void testChangeUI() {
            present(new UIChange<TestView>() {
                @Override
                public void change(TestView view) {
                    view.testChangeUI();
                }
            });
        }
    }

    private class TestView {
        public void testChangeUI() {
        }
    }
}