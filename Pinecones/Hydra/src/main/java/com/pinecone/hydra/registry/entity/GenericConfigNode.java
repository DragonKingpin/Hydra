package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;

import java.time.LocalDateTime;
import java.util.List;

public class GenericConfigNode implements ConfigNode {
    private int enumId;
    private GUID guid;
    private GUID nsGuid;
    private GUID parentGuid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    List<GenericProperties > properties;
    TextValue textValue;
    private GenericConfigNodeMeta configNodeMeta;
    private GenericNodeCommonData nodeCommonData;

    public GenericConfigNode() {
    }

    public GenericConfigNode(
            int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime,
            LocalDateTime updateTime, String name, List<GenericProperties > properties, TextValue textValue,
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

    /**
     * 获取
     * @return enumId
     */
    @Override
    public int getEnumId() {
        return this.enumId;
    }

    /**
     *
     * @param enumId
     */
    @Override
    public void setEnumId( int enumId ) {
        this.enumId = enumId;
    }

    /**
     * 获取
     * @return guid
     */
    @Override
    public GUID getGuid() {
        return this.guid;
    }

    /**
     * 设置
     * @param guid
     */
    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    /**
     * 获取
     * @return nsGuid
     */
    @Override
    public GUID getNsGuid() {
        return this.nsGuid;
    }

    /**
     * 设置设置
     * @param nsGuid
     */
    @Override
    public void setNsGuid( GUID nsGuid ) {
        this.nsGuid = nsGuid;
    }

    /**
     * 获取
     * @return parentGuid
     */
    @Override
    public GUID getParentGuid() {
        return this.parentGuid;
    }

    /**
     * 设置
     * @param parentGuid
     */
    @Override
    public void setParentGuid( GUID parentGuid ) {
        this.parentGuid = parentGuid;
    }

    /**
     * 获取
     * @return createTime
     */
    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return updateTime
     */
    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置
     * @param updateTime
     */
    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }

    /**
     * 获取
     * @return name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 设置
     * @param name
     */
    @Override
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * 获取
     * @return properties
     */
    @Override
    public List<GenericProperties > getProperties() {
        return this.properties;
    }

    /**
     * 设置
     * @param properties
     */
    @Override
    public void setProperties( List<GenericProperties> properties ) {
        this.properties = properties;
    }

    /**
     * 获取
     * @return textValue
     */
    @Override
    public TextValue getTextValue() {
        return this.textValue;
    }

    /**
     * 设置
     * @param textValue
     */
    @Override
    public void setTextValue( TextValue textValue ) {
        this.textValue = textValue;
    }

    /**
     * 获取
     * @return configNodeMeta
     */
    @Override
    public GenericConfigNodeMeta getConfigNodeMeta() {
        return this.configNodeMeta;
    }

    /**
     * 设置
     * @param configNodeMeta
     */
    @Override
    public void setConfigNodeMeta( GenericConfigNodeMeta configNodeMeta ) {
        this.configNodeMeta = configNodeMeta;
    }

    /**
     * 获取
     * @return nodeCommonData
     */
    @Override
    public GenericNodeCommonData getNodeCommonData() {
        return this.nodeCommonData;
    }

    /**
     * 设置
     * @param nodeCommonData
     */
    @Override
    public void setNodeCommonData( GenericNodeCommonData nodeCommonData ) {
        this.nodeCommonData = nodeCommonData;
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
