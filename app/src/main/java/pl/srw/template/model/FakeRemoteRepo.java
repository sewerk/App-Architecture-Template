package pl.srw.template.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeRemoteRepo implements Repository {

    private List<Todo> entries;

    public FakeRemoteRepo() {
        entries = new ArrayList<>(3);

        entries.add(new Todo(false, "add new TODO"));
        entries.add(new Todo(true, "make TODO list"));
    }

    @Override
    public Collection<Todo> get() {
        return entries;
    }

    @Override
    public boolean push(Todo todo) {
        int idx = entries.indexOf(todo);
        if (idx >= 0) {
            entries.remove(todo);
        } else {
            idx = entries.size();
        }
        entries.add(idx, todo);
        return true;
    }
}
