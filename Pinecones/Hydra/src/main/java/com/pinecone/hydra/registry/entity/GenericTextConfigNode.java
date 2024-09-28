package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class GenericTextConfigNode implements TextConfigNode{
    private int                     enumId;
    private GUID                    guid;
    private GUID                    nsGuid;
    private GUID                    parentGuid;
    private LocalDateTime           createTime;
    private LocalDateTime           updateTime;
    private String                  name;
    protected TextValue             textValue;
    protected GenericConfigNodeMeta configNodeMeta;
    protected GenericNodeCommonData nodeCommonData;
    protected DistributedRegistry   registry;

    public GenericTextConfigNode() {
    }
    public GenericTextConfigNode(DistributedRegistry registry ) {
        this.registry = registry;
    }
    public GenericTextConfigNode(int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime, LocalDateTime updateTime, String name, TextValue textValue, GenericConfigNodeMeta configNodeMeta, GenericNodeCommonData nodeCommonData, DistributedRegistry registry) {
        this.enumId = enumId;
        this.guid = guid;
        this.nsGuid = nsGuid;
        this.parentGuid = parentGuid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.name = name;
        this.textValue = textValue;
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



    public void setTextValue(TextValue textValue) {
        this.textValue = textValue;
    }

    @Override
    public void put(TextValue textValue) {
        this.registry.insertTextValue(textValue.getGuid(),textValue.getValue(),textValue.getType());
    }

    @Override
    public void remove(GUID guid) {
        this.registry.removeTextValue(guid);
    }

    @Override
    public void update(TextValue textValue) {

    }

    @Override
    public TextValue get() {
        return this.textValue;
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
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public List<Object> values() {
        return null;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Set<Property> entrySet() {
        return null;
    }


    public DistributedRegistry getRegistry() {
        return registry;
    }


    public void setRegistry(DistributedRegistry registry) {
        this.registry = registry;
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
