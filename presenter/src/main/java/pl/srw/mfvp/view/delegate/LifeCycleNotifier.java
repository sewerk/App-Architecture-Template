package pl.srw.mfvp.view.delegate;

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

    public void notifyViewReady() {
        for (LifeCycleListener listener : listeners) {
            listener.onReady();
        }
    }

    public void notifyViewRestarted() {
        for (LifeCycleListener listener : listeners) {
            listener.onRestart();
        }
    }

    public void notifyViewVisible() {
        for (LifeCycleListener listener : listeners) {
            listener.onVisible();
        }
    }

    public void notifyViewHidden() {
        for (LifeCycleListener listener : listeners) {
            listener.onHidden();
        }
    }

    public void notifyViewUnavailable() {
        for (LifeCycleListener listener : listeners) {
            listener.onUnavailable();
        }
    }

    public void notifyFinishing() {
        for (LifeCycleListener listener : listeners) {
            listener.onFinish();
        }
    }
}
