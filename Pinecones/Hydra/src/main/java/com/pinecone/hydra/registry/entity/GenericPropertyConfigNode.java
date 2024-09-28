package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class GenericPropertyConfigNode implements PropertyConfigNode{
    private int                         enumId;
    private GUID                        guid;
    private GUID                        nsGuid;
    private GUID                        parentGuid;
    private LocalDateTime               createTime;
    private LocalDateTime               updateTime;
    private String                      name;
    protected List<GenericProperty>     properties;
    protected GenericConfigNodeMeta     configNodeMeta;
    protected GenericNodeCommonData     nodeCommonData;
    protected DistributedRegistry       registry;

    public GenericPropertyConfigNode() {
    }
    public GenericPropertyConfigNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public GenericPropertyConfigNode(int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime, LocalDateTime updateTime, String name, List<GenericProperty> properties, GenericConfigNodeMeta configNodeMeta, GenericNodeCommonData nodeCommonData, DistributedRegistry registry) {
        this.enumId = enumId;
        this.guid = guid;
        this.nsGuid = nsGuid;
        this.parentGuid = parentGuid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.properties = properties;
        this.configNodeMeta = configNodeMeta;
        this.nodeCommonData = nodeCommonData;
        this.registry = registry;
    }

    public void apply( DistributedRegistry registry ) {
        this.registry = registry;
    }


    public int getEnumId() {
        return enumId;
    }


    public void setEnumId(int enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public GUID getNsGuid() {
        return nsGuid;
    }


    public void setNsGuid(GUID nsGuid) {
        this.nsGuid = nsGuid;
    }


    public GUID getParentGuid() {
        return parentGuid;
    }


    public void setParentGuid(GUID parentGuid) {
        this.parentGuid = parentGuid;
    }

    @Override
    public void put(String key, Object val) {
        Property p = new GenericProperty();
        p.setKey( key );
        p.setGuid( this.guid );

        if( val != null ) {
            p.setValue( val.toString() );
        }
        String type = PropertyTypes.queryType( val );
        p.setType( type );
        p.setCreateTime( LocalDateTime.now() );
        p.setUpdateTime( LocalDateTime.now() );

        this.putProperty( p );
    }

    @Override
    public void putProperty(Property property) {
        boolean isContain= this.containsKey(property.getKey());
        if (isContain) {
            for(Property p : this.properties){
                if (p.getKey().equals(property.getKey())){
                    p.setValue(property.getValue());
                    p.setType(property.getType());
                }
            }
            this.registry.updateProperty( property, this.guid );
        }
        else {
            this.properties.add( (GenericProperty) property );
            this.registry.insertProperties( property, this.guid );
        }

    }

    @Override
    public void remove(String key) {
        this.properties.remove(key);
        this.registry.removeProperty( this.guid, key );
    }

    @Override
    public void update(Property property) {
        for(Property p : this.properties){
            if (p.getKey().equals(property.getKey())){
                p.setValue(property.getValue());
                p.setType(property.getType());
            }
        }
        this.registry.updateProperty( property, this.guid );
    }

    @Override
    public Property get(String key) {
        for(Property p : this.properties){
            if (p.getKey().equals(key)){
                return p;
            }
        }
        return null;
    }

    @Override
    public Object getValue(String key) {
        Property property = this.get( key );
        if( property != null ) {
            return property.getValue();
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        for( Property p : this.properties ){
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
        ArrayList<Object > values = new ArrayList<>();
        for(Property p : this.properties){
            values.add(p.getValue());
        }
        return values;
    }

    @Override
    public Set<String> keySet() {
        HashSet<String > keys = new HashSet<>();
        for ( Property p : this.properties ){
            keys.add( p.getKey() );
        }
        return keys;
    }

    @Override
    public Set<Property> entrySet() {
        HashSet<Property> propertyHashSet = new HashSet<>();
        for( Property p : this.properties ){
            propertyHashSet.add(p);
        }
        return propertyHashSet;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public List<GenericProperty> getProperties() {
        return properties;
    }


    public void setProperties(List<GenericProperty> properties) {
        this.properties = properties;
    }


    public GenericConfigNodeMeta getConfigNodeMeta() {
        return configNodeMeta;
    }


    public void setConfigNodeMeta(GenericConfigNodeMeta configNodeMeta) {
        this.configNodeMeta = configNodeMeta;
    }


    public GenericNodeCommonData getNodeCommonData() {
        return nodeCommonData;
    }


    public void setNodeCommonData(GenericNodeCommonData nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
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
