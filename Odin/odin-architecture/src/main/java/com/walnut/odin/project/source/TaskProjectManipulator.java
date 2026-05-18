package com.walnut.odin.project.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.project.TaskProject;

public interface TaskProjectManipulator extends Pinenut {

    void insert( TaskProject project );

    void update( TaskProject project );

    TaskProject get( GUID guid );

    TaskProject queryByName( String szName );

    List<TaskProject> fetchByBizTreeGuid( GUID bizTreeGuid );

    long count();

    List<TaskProject> fetch( long nOffset, long nPageSize );

    long count( String szKeyword, Boolean enable );

    List<TaskProject> fetch( long nOffset, long nPageSize, String szKeyword, Boolean enable );

    default List<TaskProject> fetch() {
        return this.fetch( 0, this.count() );
    }

    void updateEnable( GUID guid, boolean bEnable );

    void remove( GUID guid );

}
