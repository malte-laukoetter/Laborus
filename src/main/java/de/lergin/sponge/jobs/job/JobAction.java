package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.listener.BreakBlockListener;
import de.lergin.sponge.jobs.listener.PlaceBlockListener;

import java.lang.reflect.Constructor;
import java.util.List;

public enum JobAction {
    BREAK(BreakBlockListener.class),
    PLACE(PlaceBlockListener.class);

    Class listener;

    JobAction(Class listener){
        this.listener = listener;
    }

    protected Class getListener(){
        return listener;
    }

    public Constructor getListenerConstructor() throws NoSuchMethodException {
        return getListener().getConstructor(Job.class, List.class);
    }
}
