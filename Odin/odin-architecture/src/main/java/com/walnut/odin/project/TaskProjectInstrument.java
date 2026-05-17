package com.walnut.odin.project;

import java.util.List;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;

public interface TaskProjectInstrument extends Instrument {

    TaskProject createProject( TaskProject project );

    TaskProject affirmProject( String szName );

    TaskProject affirmProject( String szName, String szTitle, GUID bizTreeGuid );

    TaskProject getProject( GUID guid );

    TaskProject queryProjectByName( String szName );

    List<TaskProject> fetchProjectsByBizTreeGuid( GUID bizTreeGuid );

    long countProjects();

    List<TaskProject> fetchProjects( long nOffset, long nPageSize );

    long countProjects( String szKeyword, Boolean enable );

    List<TaskProject> fetchProjects( long nOffset, long nPageSize, String szKeyword, Boolean enable );

    TaskProject updateProject( TaskProject project );

    void enableProject( GUID guid );

    void disableProject( GUID guid );

}
