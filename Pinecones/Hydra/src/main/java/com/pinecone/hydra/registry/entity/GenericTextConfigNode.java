package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;

public class GenericTextConfigNode extends ArchConfigNode implements TextConfigNode{
    protected TextValue             textValue;

    public GenericTextConfigNode() {
    }

    public GenericTextConfigNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public GenericTextConfigNode(
            int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime, LocalDateTime updateTime,
            String name, TextValue textValue, GenericConfigNodeMeta configNodeMeta, GenericNodeAttribute nodeCommonData, DistributedRegistry registry
    ) {
        super( registry, enumId, guid, nsGuid, parentGuid, createTime, updateTime, name, configNodeMeta, nodeCommonData );
        this.textValue = textValue;
    }

    @Override
    public void setTextValue(TextValue textValue) {
        this.textValue = textValue;
    }

    @Override
    public void put( TextValue textValue ) {
        this.registry.putTextValue(textValue.getGuid(),textValue.getValue(),textValue.getType());
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

    @Override
    public GenericConfigNodeMeta getConfigNodeMeta() {
        return configNodeMeta;
    }

    @Override
    public void setConfigNodeMeta(GenericConfigNodeMeta configNodeMeta) {
        this.configNodeMeta = configNodeMeta;
    }

    @Override
    public GenericNodeAttribute getNodeCommonData() {
        return this.nodeCommonData;
    }

    @Override
    public void setNodeCommonData(GenericNodeAttribute nodeCommonData) {
        this.nodeCommonData = nodeCommonData;
    }



    public DistributedRegistry getRegistry() {
        return this.registry;
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
