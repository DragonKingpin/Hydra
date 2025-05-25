package com.pinecone.tritium.system;

import com.pinecone.framework.util.config.JSONConfig;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.ware.MiddlewareDirector;
import com.pinecone.tritium.ally.rdb.RDBManager;
import com.pinecone.tritium.ally.messengers.MessagersManager;

public interface InterWareDirector extends MiddlewareDirector, HyComponent {

    @Override
    TritiumSystem getSystem();

    JSONConfig          getMiddlewareConfig();

    @Override
    default JSONConfig  getSectionConfig() {
        return this.getMiddlewareConfig();
    }

    RDBManager          getRDBManager();  // OLTP-RDB

    MessagersManager    getMessagersManager();

}
