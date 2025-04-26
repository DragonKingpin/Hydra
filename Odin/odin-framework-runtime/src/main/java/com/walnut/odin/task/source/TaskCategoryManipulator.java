package com.walnut.odin.task.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.entity.TaskCategory;

public interface TaskCategoryManipulator extends Pinenut {

    void insert( TaskCategory taskCategory );

    TaskCategory queryTaskCategory( String name );

    long countCategories();

    List<TaskCategory> fetchCategory( long offset, long pageSize );

    default List<TaskCategory> fetchCategory() {
        return this.fetchCategory( 0, this.countCategories() );
    }

    void remove( String name );

    void update( TaskCategory kernelCategory );

}
