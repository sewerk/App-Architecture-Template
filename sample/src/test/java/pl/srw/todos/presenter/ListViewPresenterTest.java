package pl.srw.todos.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;

import pl.srw.todos.model.Todo;
import pl.srw.todos.presenter.task.GetTask;
import pl.srw.todos.presenter.task.PushTask;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ListViewPresenterTest {

    @InjectMocks private ListViewPresenter sut;

    @Mock private GetTask getTask;
    @Mock private PushTask pushTask;
    @Mock private ListViewPresenter.ListView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onFirstBind_executeGetTask() throws Exception {
        // WHEN
        sut.bind(view);

        // THEN
        verify(getTask).execute(any(GetTask.Caller.class));
    }

    @Test
    public void onDataRetrieved_displayList() throws Exception {
        // GIVEN
        sut.bind(view);
        final Collection<Todo> collection = mock(Collection.class);

        // WHEN
        sut.onDataRetrieved(collection);

        // THEN
        verify(view).showEntries(collection);
    }

    @Test
    public void onNewViewRestoreState_dontExecuteGetTask() throws Exception {
        // GIVEN
        sut.bind(view);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(getTask, never()).execute();
    }

    @Test
    public void onNewViewRestoreState_displayList() throws Exception {
        // GIVEN
        sut.bind(view);

        // WHEN
        sut.onNewViewRestoreState();

        // THEN
        verify(view).showEntries(anyCollectionOf(Todo.class));
    }

    @Test
    public void checkboxClickedFor_executePushTask() throws Exception {
        // WHEN
        sut.checkboxClickedFor(mock(Todo.class));

        // THEN
        verify(pushTask).execute(any(Todo.class));
    }
}