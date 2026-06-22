package com.walnut.odin.formation.service;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.FormationFrameStatus;
import com.walnut.odin.formation.FormationPageStatus;
import com.walnut.odin.formation.FormationQueueType;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.GenericFormationGroup;
import com.walnut.odin.formation.GenericFormationGroupTask;
import com.walnut.odin.formation.GenericFormationRun;
import com.walnut.odin.formation.entity.FormationRunSubmitRequest;
import com.walnut.odin.formation.plan.GenericFormationFrame;
import com.walnut.odin.formation.plan.GenericFormationPage;
import com.walnut.odin.formation.source.FormationGroupMapper;
import com.walnut.odin.formation.source.FormationGroupTaskMapper;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;

public class FormationRunService {
    protected GuidAllocator           mGuidAllocator;
    protected FormationGroupMapper    mGroupMapper;
    protected FormationGroupTaskMapper mGroupTaskMapper;
    protected FormationRunMapper      mRunMapper;
    protected FormationRunPageMapper  mPageMapper;
    protected FormationRunFrameMapper mFrameMapper;

    public FormationRunService(
            GuidAllocator guidAllocator,
            FormationGroupMapper groupMapper,
            FormationGroupTaskMapper groupTaskMapper,
            FormationRunMapper runMapper,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper
    ) {
        this.mGuidAllocator = guidAllocator;
        this.mGroupMapper = groupMapper;
        this.mGroupTaskMapper = groupTaskMapper;
        this.mRunMapper = runMapper;
        this.mPageMapper = pageMapper;
        this.mFrameMapper = frameMapper;
    }

    public GenericFormationRun createRun( GUID formationGuid ) {
        FormationRunSubmitRequest request = new FormationRunSubmitRequest();
        request.setFormationGuid( formationGuid );
        return this.createRun( request );
    }

    public GenericFormationRun createRun( FormationRunSubmitRequest request ) {
        if ( request == null || request.getFormationGuid() == null ) {
            throw new IllegalArgumentException( "Formation run submit request has no formation guid." );
        }

        GUID formationGuid = request.getFormationGuid();
        GenericFormationGroup group = this.mGroupMapper.selectByGuid( formationGuid );
        if ( group == null ) {
            throw new IllegalArgumentException( "Formation group `" + formationGuid + "` does not exist." );
        }

        List<GenericFormationGroupTask> tasks = this.mGroupTaskMapper.fetchEnabledByFormationGuid( formationGuid );
        if ( tasks == null || tasks.isEmpty() ) {
            throw new IllegalStateException( "Formation group `" + formationGuid + "` has no enabled task." );
        }
        this.ensureManualTasks( formationGuid, tasks );

        GUID runGuid = this.mGuidAllocator.nextGUID();
        GenericFormationRun run = new GenericFormationRun();
        run.setGuid( runGuid );
        run.setFormationGuid( formationGuid );
        run.setStrategyType( this.resolveString( request.getStrategyType(), group.getStrategyType() ) );
        run.setRunStatus( FormationRunStatus.Prepared.getName() );
        run.setPageSize( Math.max( 1L, this.resolveLong( request.getPageSize(), group.getPageSize() ) ) );
        run.setFrameSize( Math.max( 1L, this.resolveLong( request.getFrameSize(), group.getFrameSize() ) ) );
        run.setWindowSize( Math.max( 1L, this.resolveLong( request.getWindowSize(), group.getWindowSize() ) ) );
        run.setInflightLimit( Math.max( 1L, this.resolveLong( request.getInflightLimit(), group.getInflightLimit() ) ) );
        run.setTotalCount( tasks.size() );
        this.mRunMapper.insert( run );

        this.expandFrames( run, tasks );
        this.expandPages( run, tasks.size() );
        return run;
    }

    protected void ensureManualTasks( GUID formationGuid, List<GenericFormationGroupTask> tasks ) {
        for ( GenericFormationGroupTask task : tasks ) {
            String scheduleType = task.getScheduleType();
            if ( scheduleType != null && !"Manual".equalsIgnoreCase( scheduleType ) ) {
                throw new IllegalStateException(
                        "Formation group `" + formationGuid + "` contains non-manual task `" + task.getTaskGuid() + "`."
                );
            }
        }
    }

    protected String resolveString( String preferred, String fallback ) {
        if ( preferred == null || preferred.trim().isEmpty() ) {
            return fallback;
        }
        return preferred;
    }

    protected long resolveLong( long preferred, long fallback ) {
        if ( preferred > 0L ) {
            return preferred;
        }
        return fallback;
    }

    protected void expandFrames( GenericFormationRun run, List<GenericFormationGroupTask> tasks ) {
        long nFrameNo = 0L;
        for ( GenericFormationGroupTask task : tasks ) {
            GenericFormationFrame frame = new GenericFormationFrame();
            frame.setGuid( this.mGuidAllocator.nextGUID() );
            frame.setRunGuid( run.getGuid() );
            frame.setFormationGuid( run.getFormationGuid() );
            frame.setGroupGuid( run.getFormationGuid() );
            frame.setTaskGuid( task.getTaskGuid() );
            frame.setTaskName( task.getTaskName() );
            frame.setPageNo( nFrameNo / run.getPageSize() );
            frame.setFrameNo( nFrameNo );
            frame.setQueueType( FormationQueueType.Ready.getName() );
            frame.setFrameStatus( FormationFrameStatus.Pending.getName() );
            frame.setPriority( task.getPriority() );
            this.mFrameMapper.insert( frame );
            ++nFrameNo;
        }
    }

    protected void expandPages( GenericFormationRun run, long totalCount ) {
        long nPageSize = Math.max( 1L, run.getPageSize() );
        long nPageCount = ( totalCount + nPageSize - 1L ) / nPageSize;
        for ( long nPageNo = 0L; nPageNo < nPageCount; ++nPageNo ) {
            long nFrameStart = nPageNo * nPageSize;
            long nFrameEnd = Math.min( totalCount, nFrameStart + nPageSize );
            GenericFormationPage page = new GenericFormationPage();
            page.setRunGuid( run.getGuid() );
            page.setFormationGuid( run.getFormationGuid() );
            page.setPageNo( nPageNo );
            page.setPageStatus( FormationPageStatus.Pending.getName() );
            page.setFrameStart( nFrameStart );
            page.setFrameEnd( nFrameEnd );
            page.setTotalCount( nFrameEnd - nFrameStart );
            this.mPageMapper.insert( page );
        }
    }
}
