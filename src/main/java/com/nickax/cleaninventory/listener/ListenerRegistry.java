package com.nickax.cleaninventory.listener;

import com.nickax.genten.listener.SwitchableListener;

import java.util.ArrayList;
import java.util.List;

public class ListenerRegistry {
    
    private static final List<SwitchableListener> listeners = new ArrayList<>();
    
    public static void registerAll() {
        listeners.forEach(SwitchableListener::enable);
    }
    
    public static void unregisterAll() {
        listeners.forEach(SwitchableListener::disable);
    }
    
    public static void add(SwitchableListener listener) {
        listeners.add(listener);
    }
    
    public static void remove(SwitchableListener listener) {
        listeners.remove(listener);
    }
}
