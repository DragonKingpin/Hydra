package com.pinecone.slime.entity;

public abstract class ArchEnumIndexableEntity implements EnumIndexableEntity {
    protected long   mnEnumId;

    protected ArchEnumIndexableEntity() {

    }

    @Override
    public long getEnumId() {
        return this.mnEnumId;
    }
}
