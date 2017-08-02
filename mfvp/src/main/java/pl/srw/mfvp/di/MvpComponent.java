package pl.srw.mfvp.di;

/**
 * Dependency injection component. Injects references to related object.
 * @param <T> associated type to be injected
 */
public interface MvpComponent<T> {

    void inject(T instance);
}
