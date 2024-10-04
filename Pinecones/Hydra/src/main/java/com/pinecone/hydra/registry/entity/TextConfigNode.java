package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface TextConfigNode extends ConfigNode {
    void setTextValue( TextValue textValue );

    void put ( TextValue textValue );

    void remove  ( GUID guid );

    void update  ( TextValue textValue );

    void update( String text, String format ) ;

    void put( String text, String format ) ;

    TextValue get ();

    void copyValueTo(GUID destinationGuid );
    void copyTo    (GUID destinationGuid);

    @Override
    default TextConfigNode evinceTextConfigNode() {
        return this;
    }
}
