package com.pinecone.slime.entity;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.id.Int64ID;

public interface EnumIndexableEntity extends ObjectiveEntity {
    @Override
    default Identification getId() {
        return new Int64ID( this.getEnumId() );
    }

    long getEnumId();
}
