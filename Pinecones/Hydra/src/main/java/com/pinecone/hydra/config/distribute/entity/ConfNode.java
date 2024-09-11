package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.Name;

import java.time.LocalDateTime;
import java.util.List;

public interface ConfNode extends TreeNode {
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
    GenericConfNodeMeta getConfNodeMeta();
    void setConfNodeMeta(GenericConfNodeMeta confNodeMeta);
    GenericNodeCommonData getNodeCommonData();
    void setNodeCommonData(GenericNodeCommonData nodeCommonData);
}
