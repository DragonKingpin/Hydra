package com.pinecone.hydra.storage.volume.core;

import java.util.Arrays;
import java.util.Optional;

public enum BuiltinStorageSupportType {
    TITAN_BLOCK(
            "TitanBlock",
            VolumePhysicalType.BLOCK,
            null,
            true,
            "{\"randomRead\":true,\"randomWrite\":true,\"sequentialRead\":true,\"sequentialWrite\":true}"
    ),
    TITAN_OBJECT(
            "TitanObject",
            VolumePhysicalType.OBJECT,
            ObjectMappedType.OBJECT_ADDRESSABLE,
            true,
            "{\"randomRead\":false,\"randomWrite\":false,\"sequentialRead\":true,\"sequentialWrite\":true,\"list\":true}"
    ),
    S3(
            "S3",
            VolumePhysicalType.OBJECT,
            ObjectMappedType.OBJECT_STORE,
            false,
            "{\"randomRead\":false,\"randomWrite\":false,\"sequentialRead\":true,\"sequentialWrite\":true,\"list\":true,\"multipart\":true}"
    ),
    HDFS(
            "HDFS",
            VolumePhysicalType.OBJECT,
            ObjectMappedType.OBJECT_ADDRESSABLE,
            false,
            "{\"randomRead\":false,\"randomWrite\":false,\"sequentialRead\":true,\"sequentialWrite\":true,\"list\":true}"
    );

    protected final String             mszDisplayName;
    protected final VolumePhysicalType mPhysicalType;
    protected final ObjectMappedType   mObjectMappedType;
    protected final boolean            mRuntimeReady;
    protected final String             mszCapabilityTemplate;

    BuiltinStorageSupportType(
            String displayName,
            VolumePhysicalType physicalType,
            ObjectMappedType objectMappedType,
            boolean runtimeReady,
            String capabilityTemplate
    ) {
        this.mszDisplayName        = displayName;
        this.mPhysicalType         = physicalType;
        this.mObjectMappedType     = objectMappedType;
        this.mRuntimeReady         = runtimeReady;
        this.mszCapabilityTemplate = capabilityTemplate;
    }

    public String getCode() {
        return this.name();
    }

    public String getDisplayName() {
        return this.mszDisplayName;
    }

    public VolumePhysicalType getPhysicalType() {
        return this.mPhysicalType;
    }

    public ObjectMappedType getObjectMappedType() {
        return this.mObjectMappedType;
    }

    public boolean isRuntimeReady() {
        return this.mRuntimeReady;
    }

    public String getCapabilityTemplate() {
        return this.mszCapabilityTemplate;
    }

    public StorageSupportDescriptor toDescriptor() {
        StorageSupportDescriptor descriptor = new StorageSupportDescriptor();
        descriptor.setCode( this.getCode() );
        descriptor.setDisplayName( this.getDisplayName() );
        descriptor.setPhysicalType( this.getPhysicalType() );
        descriptor.setObjectMappedType( this.getObjectMappedType() );
        descriptor.setBuiltin( true );
        descriptor.setRuntimeReady( this.isRuntimeReady() );
        descriptor.setCapabilityTemplate( this.getCapabilityTemplate() );
        return descriptor;
    }

    public static Optional<BuiltinStorageSupportType> find( String code ) {
        if ( code == null || code.isBlank() ) {
            return Optional.empty();
        }
        return Arrays.stream( values() )
                .filter( supportType -> supportType.name().equals( code.trim() ) )
                .findFirst();
    }
}
