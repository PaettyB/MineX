package de.keygalp.mineX.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EventExecutor {

	public static final int PRE = -1;
    public static final int ALL = 0;
    public static final int POST = 1;

    private static Map<Class<? extends IEvent>, ArrayList<EventHandler>> bindings;
    private final Set<EventListener> registeredListeners;

    private static boolean debug = true;
    
    public void setDebug(boolean debug) {
        EventExecutor.debug = debug;
    }

    public EventExecutor() {
        bindings = new HashMap<Class<? extends IEvent>, ArrayList<EventHandler>>();
        this.registeredListeners = new HashSet<EventListener>();
    }

    public List<EventHandler> getListenersFor(Class<? extends IEvent> clazz) {
        if (!bindings.containsKey(clazz)) 
            return new ArrayList<EventHandler>(); // No handlers so we return an empty list
        return new ArrayList<EventHandler>(bindings.get(clazz));
    }
    public static <T extends IEvent> T executeEvent(T event) {
    	Collection<EventHandler> handlers = bindings.get(event.getClass());
    	if (handlers == null) {
            if (debug)
               System.out.println("Handler not found!");
            return event;
        }
    	
    	for (EventHandler handler : handlers) {
            // Basic support for multi-stage events. More can be added later by specifying exactly which priority to be executed - executeEventPre(event, lessThanPriority) for example
            handler.execute(event);
        }
        return event;
    }

    public void registerListener(final EventListener listener) {
        //CustomFacade.getLog().v("Register event listener: " + listener);

        if (registeredListeners.contains(listener)) {
            //CustomFacade.getLog().w("Listener already registred: " + listener);
        	System.out.println("listener already registered");
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            Event annotation = method.getAnnotation(Event.class);
            if (annotation == null)
                continue;

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) // all listener methods should only have one parameter
                continue;

            Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
               // CustomFacade.getLog().w("Ignoring method due to non-void return: " + method.getName());
                continue;
            }

            if (IEvent.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
                Class<? extends IEvent> realParam = (Class<? extends IEvent>) param;

                if (!bindings.containsKey(realParam)) {
                    bindings.put(realParam, new ArrayList<EventHandler>());
                }
                Collection<EventHandler> eventHandlersForEvent = bindings.get(realParam);
                //CustomFacade.getLog().v("Add listener method: " + method.getName() + " for event " + realParam.getSimpleName());
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private EventHandler createEventHandler(final EventListener listener, final Method method, final Event annotation) {
        return new EventHandler(listener, method, annotation);
    }

    public void clearListeners() {
        bindings.clear();
        this.registeredListeners.clear();
    }

    public void removeListener(EventListener listener) {
        for (Entry<Class<? extends IEvent>, ArrayList<EventHandler>> ee : bindings.entrySet()) {
            Iterator<EventHandler> it = ee.getValue().iterator();
            while (it.hasNext()) {
                EventHandler curr = it.next();
                if (curr.getListener() == listener) 
                    it.remove();
            }
        }
        this.registeredListeners.remove(listener);
    }
    public Map<Class<? extends IEvent>, Collection<EventHandler>> getBindings() {
        return new HashMap<Class<? extends IEvent>, Collection<EventHandler>>(bindings);
    }
    public Set<EventListener> getRegisteredListeners() {
        return new HashSet<EventListener>(registeredListeners);
    }
}