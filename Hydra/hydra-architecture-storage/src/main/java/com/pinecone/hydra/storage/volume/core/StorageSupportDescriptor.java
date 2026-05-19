package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;

public class StorageSupportDescriptor implements Pinenut {
    protected String             mszCode;
    protected String             mszDisplayName;
    protected VolumePhysicalType mPhysicalType;
    protected ObjectMappedType   mObjectMappedType;
    protected boolean            mbBuiltin;
    protected boolean            mbRuntimeReady;
    protected String             mszCapabilityTemplate;

    public String getCode() {
        return this.mszCode;
    }

    public void setCode( String code ) {
        this.mszCode = code;
    }

    public String getDisplayName() {
        return this.mszDisplayName;
    }

    public void setDisplayName( String displayName ) {
        this.mszDisplayName = displayName;
    }

    public VolumePhysicalType getPhysicalType() {
        return this.mPhysicalType;
    }

    public void setPhysicalType( VolumePhysicalType physicalType ) {
        this.mPhysicalType = physicalType;
    }

    public ObjectMappedType getObjectMappedType() {
        return this.mObjectMappedType;
    }

    public void setObjectMappedType( ObjectMappedType objectMappedType ) {
        this.mObjectMappedType = objectMappedType;
    }

    public boolean isBuiltin() {
        return this.mbBuiltin;
    }

    public void setBuiltin( boolean builtin ) {
        this.mbBuiltin = builtin;
    }

    public boolean isRuntimeReady() {
        return this.mbRuntimeReady;
    }

    public void setRuntimeReady( boolean runtimeReady ) {
        this.mbRuntimeReady = runtimeReady;
    }

    public String getCapabilityTemplate() {
        return this.mszCapabilityTemplate;
    }

    public void setCapabilityTemplate( String capabilityTemplate ) {
        this.mszCapabilityTemplate = capabilityTemplate;
    }
}
