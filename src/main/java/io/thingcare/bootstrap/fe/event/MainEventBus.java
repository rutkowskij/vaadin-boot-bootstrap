package io.thingcare.bootstrap.fe.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import io.thingcare.bootstrap.fe.ui.MainUI;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class MainEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        MainUI.mainEventBus().eventBus.post(event);
    }

    public static void register(final Object object) {
        MainUI.mainEventBus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        MainUI.mainEventBus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,
                                      final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
