package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenericConfigNode implements ConfigNode {
    private int enumId;
    private GUID guid;
    private GUID nsGuid;
    private GUID parentGuid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    List<GenericProperty> properties;
    TextValue textValue;
    private GenericConfigNodeMeta configNodeMeta;
    private GenericNodeCommonData nodeCommonData;

    public GenericConfigNode() {
    }

    public GenericConfigNode(
            int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime,
            LocalDateTime updateTime, String name, List<GenericProperty> properties, TextValue textValue,
            GenericConfigNodeMeta configNodeMeta, GenericNodeCommonData nodeCommonData
    ) {
        this.enumId = enumId;
        this.guid = guid;
        this.nsGuid = nsGuid;
        this.parentGuid = parentGuid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.properties = properties;
        this.textValue = textValue;
        this.configNodeMeta = configNodeMeta;
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public int getEnumId() {
        return this.enumId;
    }


    @Override
    public void setEnumId( int enumId ) {
        this.enumId = enumId;
    }


    @Override
    public GUID getGuid() {
        return this.guid;
    }


    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }


    @Override
    public GUID getNsGuid() {
        return this.nsGuid;
    }


    @Override
    public void setNsGuid( GUID nsGuid ) {
        this.nsGuid = nsGuid;
    }


    @Override
    public GUID getParentGuid() {
        return this.parentGuid;
    }


    @Override
    public void setParentGuid( GUID parentGuid ) {
        this.parentGuid = parentGuid;
    }


    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }


    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }


    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }


    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }


    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void setName( String name ) {
        this.name = name;
    }


    @Override
    public List<GenericProperty> getProperties() {
        return this.properties;
    }


    @Override
    public void setProperties( List<GenericProperty> properties ) {
        this.properties = properties;
    }


    @Override
    public TextValue getTextValue() {
        return this.textValue;
    }


    @Override
    public void setTextValue( TextValue textValue ) {
        this.textValue = textValue;
    }


    @Override
    public GenericConfigNodeMeta getConfigNodeMeta() {
        return this.configNodeMeta;
    }


    @Override
    public void setConfigNodeMeta( GenericConfigNodeMeta configNodeMeta ) {
        this.configNodeMeta = configNodeMeta;
    }


    @Override
    public GenericNodeCommonData getNodeCommonData() {
        return this.nodeCommonData;
    }


    @Override
    public void setNodeCommonData( GenericNodeCommonData nodeCommonData ) {
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public void putProperty(Property property, DistributedRegistry registry) {
        this.properties.add((GenericProperty) property);
        registry.insertProperties(property,this.guid);
    }

    @Override
    public void removeProperty(String key, DistributedRegistry registry) {
        this.properties.remove(key);
        registry.removeProperty(this.guid,key);
    }

    @Override
    public void updateProperty(Property property, DistributedRegistry registry) {
        for(Property p : this.properties){
            if (p.getKey().equals(property.getKey())){
                p.setValue(property.getValue());
                p.setType(property.getType());
            }
        }
        registry.updateProperty(property,this.guid);

    }

    @Override
    public Property getProperty(String key) {
        for(Property p : this.properties){
            if (p.getKey().equals(key)){
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        for(Property p : this.properties){
            if (p.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.properties.size();
    }

    @Override
    public boolean isEmpty() {
        return !this.properties.isEmpty();
    }

    @Override
    public List<Object> values() {
        ArrayList<Object> values = new ArrayList<>();
        for(Property p : this.properties){
            values.add(p.getValue());
        }
        return values;
    }

    @Override
    public Set<String> keySet() {
        HashSet<String> keys = new HashSet<>();
        for (Property p : this.properties){
            keys.add(p.getKey());
        }
        return keys;
    }

    @Override
    public Set<Property> entrySet() {
        HashSet<Property> propertyHashSet = new HashSet<>();
        for(Property p : this.properties){
            propertyHashSet.add(p);
        }
        return propertyHashSet;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
