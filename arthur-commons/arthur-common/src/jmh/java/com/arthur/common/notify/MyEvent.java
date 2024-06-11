package com.arthur.common.notify;

import com.arthur.common.notify.event.Event;

import java.io.Serial;

public class MyEvent extends Event {

    @Serial
	private static final long serialVersionUID = 1527777972238440237L;

    protected MyEvent(String eventObject) {
        super(eventObject);
    }

}
