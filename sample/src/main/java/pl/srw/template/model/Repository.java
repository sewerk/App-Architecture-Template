package pl.srw.template.model;

import java.util.Collection;

/**
 * Repository for {@link Todo} entries
 */
public interface Repository {

    Collection<Todo> get();

    boolean push(Todo todo);
}
