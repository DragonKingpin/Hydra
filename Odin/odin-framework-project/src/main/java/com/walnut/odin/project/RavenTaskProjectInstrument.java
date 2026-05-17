package com.walnut.odin.project;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;
import com.walnut.odin.project.source.TaskProjectManipulator;

public class RavenTaskProjectInstrument implements TaskProjectInstrument {

    protected TaskProjectManipulator mTaskProjectManipulator;

    protected GuidAllocator          mGuidAllocator;

    public RavenTaskProjectInstrument( TaskProjectManipulator taskProjectManipulator ) {
        this( taskProjectManipulator, new GuidAllocator128V7() );
    }

    public RavenTaskProjectInstrument( TaskProjectManipulator taskProjectManipulator, GuidAllocator guidAllocator ) {
        this.mTaskProjectManipulator = taskProjectManipulator;
        this.mGuidAllocator          = guidAllocator;
    }

    protected void assertProjectName( String szName ) {
        if ( szName == null || szName.trim().isEmpty() ) {
            throw new IllegalArgumentException( "Project name is blank." );
        }
    }

    protected void prepareProject( TaskProject project ) {
        if ( project == null ) {
            throw new IllegalArgumentException( "TaskProject is null." );
        }
        this.assertProjectName( project.getName() );
        if ( project.getGuid() == null ) {
            project.setGuid( this.mGuidAllocator.nextGUID() );
        }
    }

    @Override
    public TaskProject createProject( TaskProject project ) {
        this.prepareProject( project );
        this.mTaskProjectManipulator.insert( project );
        return project;
    }

    @Override
    public TaskProject affirmProject( String szName ) {
        return this.affirmProject( szName, null, null );
    }

    @Override
    public TaskProject affirmProject( String szName, String szTitle, GUID bizTreeGuid ) {
        this.assertProjectName( szName );
        TaskProject project = this.mTaskProjectManipulator.queryByName( szName );
        if ( project != null ) {
            return project;
        }

        GenericTaskProject newProject = new GenericTaskProject();
        newProject.setName( szName );
        newProject.setTitle( szTitle );
        newProject.setBizTreeGuid( bizTreeGuid );
        return this.createProject( newProject );
    }

    @Override
    public TaskProject getProject( GUID guid ) {
        return this.mTaskProjectManipulator.get( guid );
    }

    @Override
    public TaskProject queryProjectByName( String szName ) {
        this.assertProjectName( szName );
        return this.mTaskProjectManipulator.queryByName( szName );
    }

    @Override
    public List<TaskProject> fetchProjectsByBizTreeGuid( GUID bizTreeGuid ) {
        return this.mTaskProjectManipulator.fetchByBizTreeGuid( bizTreeGuid );
    }

    @Override
    public long countProjects() {
        return this.mTaskProjectManipulator.count();
    }

    @Override
    public List<TaskProject> fetchProjects( long nOffset, long nPageSize ) {
        return this.mTaskProjectManipulator.fetch( nOffset, nPageSize );
    }

    @Override
    public long countProjects( String szKeyword, Boolean enable ) {
        return this.mTaskProjectManipulator.count( szKeyword, enable );
    }

    @Override
    public List<TaskProject> fetchProjects( long nOffset, long nPageSize, String szKeyword, Boolean enable ) {
        return this.mTaskProjectManipulator.fetch( nOffset, nPageSize, szKeyword, enable );
    }

    @Override
    public TaskProject updateProject( TaskProject project ) {
        if ( project == null || project.getGuid() == null ) {
            throw new IllegalArgumentException( "TaskProject guid is null." );
        }
        this.mTaskProjectManipulator.update( project );
        return this.mTaskProjectManipulator.get( project.getGuid() );
    }

    @Override
    public void enableProject( GUID guid ) {
        this.mTaskProjectManipulator.updateEnable( guid, true );
    }

    @Override
    public void disableProject( GUID guid ) {
        this.mTaskProjectManipulator.updateEnable( guid, false );
    }

}
