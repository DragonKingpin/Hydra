package com.walnut.sparta.ucdn.console.umc.ufm.protocol;

public class RequestHead {
    protected long sessionId;


    public RequestHead setSessionId(long sessionId ) {
        this.sessionId = sessionId;
        return this;
    }

    public long getSessionId() {
        return this.sessionId;
    }


    public static RequestHead newRequest() {
        return new RequestHead();
    }
}
