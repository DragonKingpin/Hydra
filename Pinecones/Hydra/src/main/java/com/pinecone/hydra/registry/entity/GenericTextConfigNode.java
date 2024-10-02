package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;

public class GenericTextConfigNode extends ArchConfigNode implements TextConfigNode{
    protected TextValue             mTextValue;

    public GenericTextConfigNode() {
    }

    public GenericTextConfigNode( DistributedRegistry registry ) {
        this.registry = registry;
    }

    public GenericTextConfigNode(
            int enumId, GUID guid, GUID nsGuid, GUID dataAffinityGuid, LocalDateTime createTime, LocalDateTime updateTime,
            String name, TextValue textValue, GenericConfigNodeMeta configNodeMeta, GenericNodeAttribute nodeCommonData, DistributedRegistry registry
    ) {
        super( registry, enumId, guid, dataAffinityGuid, createTime, updateTime, name, configNodeMeta, nodeCommonData );
        this.mTextValue = textValue;
    }

    @Override
    public void setTextValue(TextValue textValue) {
        this.mTextValue = textValue;
    }

    @Override
    public void put( TextValue textValue ) {
        if( this.mTextValue == null ) {
            this.registry.putTextValue( textValue.getGuid(), textValue.getValue(), textValue.getType() );
        }
        else {
            this.update( textValue );
            this.mTextValue = textValue;
        }
    }

    @Override
    public void remove( GUID guid ) {
        this.registry.removeTextValue(guid);
    }

    @Override
    public void update( TextValue textValue ) {
        this.registry.updateTextValue( textValue, this.guid );
    }


    @Override
    public void update( String text, String format ) {
        TextValue textValue = GenericTextValue.newUpdateTextValue( this.guid, text, format );
        this.update( textValue );
    }

    @Override
    public void put( String text, String format ) {
        if( this.mTextValue == null ) {
            this.registry.putTextValue( this.guid, text, format );
        }
        else {
            this.update( text, format );
        }
    }

    @Override
    public TextValue get() {
        return this.mTextValue;
    }

    @Override
    public GenericConfigNodeMeta getConfigNodeMeta() {
        return this.configNodeMeta;
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
