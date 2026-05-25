package com.walnut.odin.specific;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigest;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigestQuery;
import com.walnut.odin.specific.digest.TaskSpecificPage;
import com.walnut.odin.specific.digest.TaskSpecificDigest;
import com.walnut.odin.specific.digest.TaskSpecificDigestQuery;
import com.walnut.odin.specific.source.TaskSpecificManipulator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RavenTaskSpecificService implements TaskSpecificService {

    protected TaskSpecificManipulator mTaskSpecificManipulator;

    public RavenTaskSpecificService( TaskSpecificManipulator taskProjectDigestManipulator ) {
        this.mTaskSpecificManipulator = taskProjectDigestManipulator;
    }

    @Override
    public TaskSpecificPage<TaskSpecificDigest> pageTaskSpecificDigests( TaskSpecificDigestQuery query ) {
        if ( query == null ) {
            query = new TaskSpecificDigestQuery();
        }
        long nTotal = this.mTaskSpecificManipulator.countTaskSpecificDigests( query );
        List<TaskSpecificDigest> items = this.mTaskSpecificManipulator.fetchTaskSpecificDigests( query );
        return new TaskSpecificPage<>( items, nTotal, query.getOffset(), query.getLimit() );
    }

    @Override
    public List<TaskSpecificDigest> fetchTaskSpecificDigestsByGuids( Collection<GUID> taskGuids ) {
        if ( taskGuids == null || taskGuids.isEmpty() ) {
            return Collections.emptyList();
        }
        return this.mTaskSpecificManipulator.fetchTaskSpecificDigestsByGuids( taskGuids );
    }

    @Override
    public TaskSpecificPage<TaskInstanceSpecificDigest> pageTaskInstanceSpecificDigests( TaskInstanceSpecificDigestQuery query ) {
        if ( query == null ) {
            query = new TaskInstanceSpecificDigestQuery();
        }
        long nTotal = this.mTaskSpecificManipulator.countTaskInstanceSpecificDigests( query );
        List<TaskInstanceSpecificDigest> items = this.mTaskSpecificManipulator.fetchTaskInstanceSpecificDigests( query );
        return new TaskSpecificPage<>( items, nTotal, query.getOffset(), query.getLimit() );
    }

    @Override
    public String queryTaskProjectGuid( GUID taskGuid ) {
        if ( taskGuid == null ) {
            return null;
        }
        return this.mTaskSpecificManipulator.selectTaskProjectGuid( taskGuid );
    }

    @Override
    public void bindTaskProject( GUID taskGuid, GUID projectGuid ) {
        if ( taskGuid == null || projectGuid == null ) {
            return;
        }
        this.mTaskSpecificManipulator.updateTaskProjectGuid( taskGuid, projectGuid );
    }
}
