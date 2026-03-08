package com.walnut.odin.task.dto;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.entity.EnumIndexableEntity;

public interface CategoryTag extends EnumIndexableEntity {

    void setEnumId( long id );

    void setTaskGuid( GUID taskGuid );

    GUID getTaskGuid();

    void setCategoryName( String categoryName );

    String getCategoryName();

    void setCategoryType( String categoryType );

    String getCategoryType();

}
