package com.arthur.common.notify.event;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An abstract class for event.
 */
public abstract class Event implements Serializable {

    @Serial
	private static final long serialVersionUID = 7514712671324406191L;

    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    private final long sequence = SEQUENCE.getAndIncrement();

    private final Object eventObject;

    protected Event(Object eventObject) {
        this.eventObject = eventObject;
    }

    public long getSequence() {
        return sequence;
    }

    public String scope() {
        return null;
    }

    public Object getEventObject() {
        return eventObject;
    }

}
