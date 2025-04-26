package com.walnut.odin.task.entity;

import com.pinecone.slime.entity.EnumIndexableEntity;

public interface Category extends EnumIndexableEntity {

    void setEnumId( long id );

    void setName( String name ) ;

    String getName() ;

    void setAlias( String alias ) ;

    String getAlias() ;

    void setDescription( String description ) ;

    String getDescription() ;

}
