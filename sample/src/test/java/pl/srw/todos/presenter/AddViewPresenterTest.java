package pl.srw.todos.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.todos.model.Todo;
import pl.srw.todos.presenter.task.PushTask;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class AddViewPresenterTest {

    @InjectMocks private AddViewPresenter sut;

    @Mock private PushTask pushTask;
    @Mock private AddViewPresenter.AddView view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addClicked_executePushTask() throws Exception {
        // WHEN
        sut.addClicked(false, "");

        // THEN
        verify(pushTask).execute(any(Todo.class));
    }

    @Test
    public void addClicked_closeView() throws Exception {
        // GIVEN
        sut.bind(view);

        // WHEN
        sut.addClicked(false, "");

        // THEN
        verify(view).close();
    }
}