package pl.srw.template.core.view.delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifies registered listeners about lifecycle events
 */
public class LifeCycleNotifier {

    private List<LifeCycleListener> listeners;

    public LifeCycleNotifier() {
        this.listeners = new ArrayList<>(1);
    }

    public void register(LifeCycleListener listener) {
        listeners.add(listener);
    }

    public void notifyOnStart() {
        for (LifeCycleListener listener : listeners) {
            listener.onStart();
        }
    }

    public void notifyOnStop() {
        for (LifeCycleListener listener : listeners) {
            listener.onStop();
        }
    }

    public void notifyOnEnd() {
        for (LifeCycleListener listener : listeners) {
            listener.onEnd();
        }
    }
}
