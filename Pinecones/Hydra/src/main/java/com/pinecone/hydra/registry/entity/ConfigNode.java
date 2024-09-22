package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.unit.udtt.entity.EvinceTreeNode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ConfigNode extends EvinceTreeNode {
    @Override
    default ConfigNode evinceRegistryTreeNode() {
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

    List<GenericProperty> getProperties();

    void setProperties(List<GenericProperty> properties);

    TextValue getTextValue();

    void setTextValue(TextValue textValue);

    GenericConfigNodeMeta getConfigNodeMeta();

    void setConfigNodeMeta(GenericConfigNodeMeta configNodeMeta);

    GenericNodeCommonData getNodeCommonData();

    void setNodeCommonData(GenericNodeCommonData nodeCommonData);


    void putProperty(Property property, DistributedRegistry registry);
    void removeProperty(String key, DistributedRegistry registry);
    void updateProperty(Property property, DistributedRegistry registry);
    Property getProperty(String key);
    boolean containsKey(String key);


    int size();
    boolean isEmpty();
    List<Object> values();
    Set<String> keySet();
    Set<Property> entrySet();

}
