package com.pinecone.hydra.device.registry.server.detached;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceDetachedObservationEntry implements Pinenut {

    protected Long clientId;

    protected GUID instanceGuid;

    protected GUID deviceGuid;

    protected String connectionId;

    protected long detachedAtMillis;

    protected long deadlineMillis;

    protected Object caused;

    public DeviceDetachedObservationEntry(
            Long clientId,
            GUID instanceGuid,
            GUID deviceGuid,
            String connectionId,
            long detachedAtMillis,
            long deadlineMillis,
            Object caused
    ) {
        this.clientId = clientId;
        this.instanceGuid = instanceGuid;
        this.deviceGuid = deviceGuid;
        this.connectionId = connectionId;
        this.detachedAtMillis = detachedAtMillis;
        this.deadlineMillis = deadlineMillis;
        this.caused = caused;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    public String getConnectionId() {
        return this.connectionId;
    }

    public long getDetachedAtMillis() {
        return this.detachedAtMillis;
    }

    public long getDeadlineMillis() {
        return this.deadlineMillis;
    }

    public Object getCaused() {
        return this.caused;
    }
}
