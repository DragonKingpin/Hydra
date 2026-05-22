package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;

public class GrpcRemoteProcessControlSession implements RemoteProcessControlSession {

    protected long      mnClientId;

    protected String    mszSessionGuid;

    protected boolean   mbActive;

    protected Object    mResponseObserver;

    public GrpcRemoteProcessControlSession( long clientId, String szSessionGuid, Object responseObserver ) {
        this.mnClientId         = clientId;
        this.mszSessionGuid     = szSessionGuid;
        this.mResponseObserver  = responseObserver;
        this.mbActive           = true;
    }

    public String sessionGuid() {
        return this.mszSessionGuid;
    }

    public Object responseObserver() {
        return this.mResponseObserver;
    }

    public void close() {
        this.mbActive = false;
    }

    @Override
    public long clientId() {
        return this.mnClientId;
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Grpc;
    }

    @Override
    public boolean isActive() {
        return this.mbActive;
    }

}
