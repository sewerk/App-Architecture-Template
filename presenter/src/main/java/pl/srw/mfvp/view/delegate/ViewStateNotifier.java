package pl.srw.mfvp.view.delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Notifies registered listeners about lifecycle events
 */
public class ViewStateNotifier {

    private List<ViewStateListener> listeners;

    public ViewStateNotifier() {
        this.listeners = new ArrayList<>(1);
    }

    public void register(ViewStateListener listener) {
        listeners.add(listener);
    }

    public void notifyViewReady() {
        for (ViewStateListener listener : listeners) {
            listener.onReady();
        }
    }

    public void notifyViewVisible() {
        for (ViewStateListener listener : listeners) {
            listener.onVisible();
        }
    }

    public void notifyViewHidden() {
        for (ViewStateListener listener : listeners) {
            listener.onHidden();
        }
    }

    public void notifyViewUnavailable() {
        for (ViewStateListener listener : listeners) {
            listener.onUnavailable();
        }
    }

    public void notifyFinishing() {
        for (ViewStateListener listener : listeners) {
            listener.onFinish();
        }
    }
}
