package com.pinecone.radium.system;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.ware.MiddlewareDirector;
import com.pinecone.radium.ally.rdb.RDBManager;
import com.pinecone.radium.ally.messengers.MessagersManager;

public interface InterWareDirector extends MiddlewareDirector, HyComponent {

    @Override
    RadiumSystem        getSystem();

    JSONConfig          getMiddlewareConfig();

    @Override
    default JSONConfig  getSectionConfig() {
        return this.getMiddlewareConfig();
    }

    RDBManager          getRDBManager();  // OLTP-RDB

    MessagersManager    getMessagersManager();

}
