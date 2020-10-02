package de.keygalp.mineX.events;

import java.lang.reflect.Method;

public class EventHandler {

	private final EventListener listener;
    private final Method method;
    private final Event annotation;

    public EventHandler(EventListener listener, Method method, Event annotation) {
    	System.out.println("new handler");
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }

    public Event getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public EventListener getListener() {
        return listener;
    }

    public void execute(IEvent event) {
        try {
            method.invoke(listener, event);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return "(EventHandler " + this.listener + ": " + method.getName() + ")";
    }

}
