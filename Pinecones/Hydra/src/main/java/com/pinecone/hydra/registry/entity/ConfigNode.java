package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;
import java.time.LocalDateTime;

public interface ConfigNode extends RegistryTreeNode {
    @Override
    default ConfigNode evinceConfigNode() {
        return this;
    }

    int getEnumId();

    void setEnumId(int enumId);

    GUID getGuid();

    void setGuid(GUID guid);

    GUID getNsGuid();

    void setNsGuid(GUID guid);


    GUID getParentGuid();

    void setParentGuid(GUID guid);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    String getName();

    void setName(String name);

//    List<GenericProperty > getProperties();
//
//    void setProperties(List<GenericProperty> properties);

//    TextValue getTextValue();
//
//    void setTextValue(TextValue textValue);

    ConfigNodeMeta getConfigNodeMeta();

    void setConfigNodeMeta( GenericConfigNodeMeta configNodeMeta );

    NodeAttribute getNodeCommonData();

    void setNodeCommonData( GenericNodeAttribute nodeCommonData );


    DistributedRegistry getRegistry();


}
