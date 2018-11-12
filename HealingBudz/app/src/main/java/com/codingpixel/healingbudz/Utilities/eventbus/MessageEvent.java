package com.codingpixel.healingbudz.Utilities.eventbus;

/**
 * Created by incubasyss on 16/02/2018.
 */

public class MessageEvent {
    private final Boolean isNotify;

    public MessageEvent(boolean isNotify) {
        this.isNotify = isNotify;
    }

    public Boolean getNotify() {
        return isNotify;
    }

    /* Additional fields if needed */
}
