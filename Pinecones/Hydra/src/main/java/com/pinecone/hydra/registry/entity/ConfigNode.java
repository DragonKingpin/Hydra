package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.DistributedRegistry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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

    void setConfigNodeMeta(GenericConfigNodeMeta configNodeMeta);

    NodeCommonData getNodeCommonData();

    void setNodeCommonData( GenericNodeCommonData nodeCommonData );


//    void put             ( String key, Object val );
//    void putProperty     ( Property property );
//    void removeProperty  ( String key );
//    void updateProperty  ( Property property );
//    Property getProperty ( String key );
//    Object getValue      ( String key );

    boolean containsKey  ( String key );


    int size();
    boolean isEmpty();
    List<Object > values();
    Set<String > keySet();
    Set<Property > entrySet();

    DistributedRegistry getRegistry();


}
