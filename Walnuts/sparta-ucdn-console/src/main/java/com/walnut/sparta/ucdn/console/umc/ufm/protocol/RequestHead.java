package com.walnut.sparta.ucdn.console.umc.ufm.protocol;

public class RequestHead {
    protected long sessionId;

    protected long requestId;


    public RequestHead setSessionId(long sessionId ) {
        this.sessionId = sessionId;
        return this;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public RequestHead setRequestId( long requestId ) {
        this.requestId = requestId;
        return this;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public static RequestHead newRequest() {
        return new RequestHead();
    }
}
