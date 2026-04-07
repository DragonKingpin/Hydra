package com.walnut.odin.task.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.entity.pyramid.CategoryType;


public interface CategoryTypeManipulator extends Pinenut {

    void insert( CategoryType categoryType );

    CategoryType queryType( String name );

    long countTypes();

    List<CategoryType> fetchType( long offset, long pageSize );

    default List<CategoryType> fetchType() {
        return this.fetchType( 0, this.countTypes() );
    }

    void remove( String name );

    void update( CategoryType categoryType );

}
