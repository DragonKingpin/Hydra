package com.acorn.redqueen.service.purge;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class PurgeSafetyReport implements Pinenut {
    protected boolean purgeable;
    protected boolean purged;
    protected String serviceGuid;
    protected String servicePath;
    protected String serviceName;
    protected long instanceCount;
    protected final List<PurgeBlocker> blockers = new ArrayList<>();

    public boolean isPurgeable() {
        return this.purgeable;
    }

    public void setPurgeable( boolean purgeable ) {
        this.purgeable = purgeable;
    }

    public boolean isPurged() {
        return this.purged;
    }

    public void setPurged( boolean purged ) {
        this.purged = purged;
    }

    public String getServiceGuid() {
        return this.serviceGuid;
    }

    public void setServiceGuid( String serviceGuid ) {
        this.serviceGuid = serviceGuid;
    }

    public String getServicePath() {
        return this.servicePath;
    }

    public void setServicePath( String servicePath ) {
        this.servicePath = servicePath;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName( String serviceName ) {
        this.serviceName = serviceName;
    }

    public long getInstanceCount() {
        return this.instanceCount;
    }

    public void setInstanceCount( long instanceCount ) {
        this.instanceCount = instanceCount;
    }

    public List<PurgeBlocker> getBlockers() {
        return this.blockers;
    }

    public void addBlocker( String type, String targetGuid, String status, String message ) {
        this.blockers.add( new PurgeBlocker( type, targetGuid, status, message ) );
        this.purgeable = false;
    }
}
