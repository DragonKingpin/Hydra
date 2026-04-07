package com.walnut.odin.task.source;


import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.task.dto.CategoryTag;

public interface CategoryMappingManipulator extends Pinenut {

    void insert( CategoryTag categoryTag );

    List<CategoryTag> queryByTaskGuid ( GUID taskGuid );

    CategoryTag queryOwnedTag( GUID taskGuid, String type, String name );

    long countTag( String type, String name );

    List<CategoryTag> queryTag ( String type, String name, long offset, long pageSize );

    long countTagsByName( String name );

    List<CategoryTag> fetchByName ( String name, long offset, long pageSize );

    void update( CategoryTag categoryTag );

    void purge( GUID taskGuid, String type, String name );

    default void remove( GUID taskGuid, String type, String name ) {
        this.purge( taskGuid, type, name );
    }

    default void purgeByName( String name ) {
        this.purge( null, null, name );
    }

    default void purgeByTaskGuid( GUID taskGuid ) {
        this.purge( taskGuid, null, null );
    }


}
