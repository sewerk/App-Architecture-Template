package pl.srw.todos.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainViewPresenterTest {

    @InjectMocks private MainViewPresenter sut;

    @Mock private MainViewPresenter.MainView view;

    @Before
    public void setUp() throws Exception {
        sut = new MainViewPresenter();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onFirstBind_showListView() throws Exception {
        // WHEN
        sut.onFirstBind();

        // THEN
        verify(view).showListView();
    }

    @Test
    public void onNewRestoreState_ifAddViewShown_hidesAddButton() throws Exception {
        // GIVEN
        sut.addClicked();

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view, times(2)).hideAddButton();
    }

    @Test
    public void onNewRestoreState_ifListViewShown_dontHideAddButton() throws Exception {
        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view, never()).hideAddButton();
    }

    @Test
    public void addClicked_addViewIsShown() throws Exception {
        // WHEN
        sut.addClicked();

        // THEN
        verify(view).showAddView();
    }

    @Test
    public void addClicked_addButtonHidden() throws Exception {
        // WHEN
        sut.addClicked();

        // THEN
        verify(view).hideAddButton();
    }

    @Test
    public void backPressed_ifAddViewShown_showAddButton() throws Exception {
        // GIVEN
        sut.addClicked();

        // WHEN
        sut.backPressed();

        // THEN
        verify(view).showAddButton();
    }

    @Test
    public void backPressed_ifListViewShown_dontShowAddButton() throws Exception {
        // WHEN
        sut.backPressed();

        // THEN
        verify(view, never()).showAddButton();
    }
}