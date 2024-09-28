package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface TextConfigNode extends ConfigNode {
    void setTextValue(TextValue textValue);
    void put ( TextValue textValue );
    void remove  ( GUID guid );
    void update  ( TextValue textValue );
    TextValue get ();
    @Override
    default TextConfigNode evinceTextConfigNode() {
        return this;
    }


}
