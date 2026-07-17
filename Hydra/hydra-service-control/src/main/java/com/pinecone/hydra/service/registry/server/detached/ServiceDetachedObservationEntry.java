package com.pinecone.hydra.service.registry.server.detached;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceDetachedObservationEntry implements Pinenut {

    protected Long clientId;

    protected long observationId;

    protected GUID instanceGuid;

    protected GUID serviceGuid;

    protected long detachedAtMillis;

    protected long deadlineMillis;

    protected Object caused;

    public ServiceDetachedObservationEntry(
            Long clientId,
            long observationId,
            GUID instanceGuid,
            GUID serviceGuid,
            long detachedAtMillis,
            long deadlineMillis,
            Object caused
    ) {
        this.clientId = clientId;
        this.observationId = observationId;
        this.instanceGuid = instanceGuid;
        this.serviceGuid = serviceGuid;
        this.detachedAtMillis = detachedAtMillis;
        this.deadlineMillis = deadlineMillis;
        this.caused = caused;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public long getObservationId() {
        return this.observationId;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public GUID getServiceGuid() {
        return this.serviceGuid;
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
