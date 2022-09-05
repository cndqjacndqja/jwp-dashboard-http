package org.apache.coyote.http11.model.session;

public class Session {

    private final String key;
    private final Object value;

    public Session(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }
}
