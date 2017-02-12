package pl.srw.todos.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Collection;

import javax.inject.Provider;

import pl.srw.todos.model.Todo;
import pl.srw.todos.presenter.task.GetTask;
import pl.srw.todos.presenter.task.PushTask;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ListViewPresenterTest {

    @InjectMocks private ListViewPresenter sut;

    @Mock private GetTask getTask;
    @Mock private PushTask pushTask;
    @Mock private Provider<PushTask> pushTaskProvider;
    @Mock private ListViewPresenter.ListView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Whitebox.setInternalState(sut, "view", view);
        when(pushTaskProvider.get()).thenReturn(pushTask);
    }

    @Test
    public void constructor_executeGetTask() throws Exception {
        // THEN
        verify(getTask).execute(any(GetTask.Caller.class));
    }

    @Test
    public void onDataRetrieved_displayList() throws Exception {
        // GIVEN
        final Collection<Todo> collection = mock(Collection.class);

        // WHEN
        sut.onDataRetrieved(collection);

        // THEN
        verify(view).showEntries(collection);
    }

    @Test
    public void onNewViewRestoreState_dontExecuteGetTask() throws Exception {
        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(getTask, never()).execute();
    }

    @Test
    public void onNewViewRestoreState_whenEntriesRetrieved_displayList() throws Exception {
        // GIVEN
        final Collection<Todo> collection = mock(Collection.class);
        Whitebox.setInternalState(sut, "entries", collection);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).showEntries(collection);
    }

    @Test
    public void onDataRetrieved_afterOnNewRestoreState_displayListOnce() throws Exception {
        // GIVEN
        final Collection<Todo> collection = mock(Collection.class);

        // WHEN
        sut.onNewViewRestoreState();
        sut.onDataRetrieved(collection);

        // THEN
        verify(view).showEntries(collection);
    }

    @Test
    public void onDataRetrieved_beforeOnNewRestoreState_displayListTwice() throws Exception {
        // GIVEN
        final Collection<Todo> collection = mock(Collection.class);

        // WHEN
        sut.onDataRetrieved(collection);
        sut.onNewViewRestoreState();

        // THEN
        verify(view, times(2)).showEntries(collection);
    }

    @Test
    public void checkboxClickedFor_executePushTask() throws Exception {
        // WHEN
        sut.checkboxClickedFor(mock(Todo.class));

        // THEN
        verify(pushTask).execute(any(Todo.class));
    }
}