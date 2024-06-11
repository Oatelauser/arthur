package com.arthur.common.notify.event;

/**
 * create slow event instance factory
 */
public class SlowEvent<T> extends DataEvent<T> {

    private String sequence;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

}
