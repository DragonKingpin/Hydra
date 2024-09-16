package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;

import java.time.LocalDateTime;
import java.util.List;

public interface ConfigNode extends TreeNode {
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

    List<GenericProperties> getProperties();

    void setProperties(List<GenericProperties> properties);

    TextValue getTextValue();

    void setTextValue(TextValue textValue);

    GenericConfigNodeMeta getConfigNodeMeta();

    void setConfigNodeMeta(GenericConfigNodeMeta configNodeMeta);

    GenericNodeCommonData getNodeCommonData();

    void setNodeCommonData(GenericNodeCommonData nodeCommonData);
}
