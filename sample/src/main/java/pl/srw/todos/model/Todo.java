package pl.srw.todos.model;

import java.util.Objects;

/**
 * Data domain model
 */
public class Todo {

    private boolean done;
    private String text;

    public Todo(boolean done, String text) {
        this.done = done;
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        Todo todo = (Todo) o;
        return Objects.equals(text, todo.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
