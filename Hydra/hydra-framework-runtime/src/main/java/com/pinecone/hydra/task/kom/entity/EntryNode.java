package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.unit.imperium.entity.MetaEntryNode;
import com.pinecone.slime.entity.EnumIndexableEntity;

public interface EntryNode extends MetaEntryNode, EnumIndexableEntity {

    @Override
    default EntryNode evinceEntryNode() {
        return this;
    }

}
