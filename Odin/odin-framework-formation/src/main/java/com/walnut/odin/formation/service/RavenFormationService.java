package com.walnut.odin.formation.service;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.FormationInstrument;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.deletion.FormationGroupPurgeResult;
import com.walnut.odin.formation.entity.GroupEntry;
import com.walnut.odin.formation.entity.GroupTaskEntry;
import com.walnut.odin.formation.entity.GenericRun;
import com.walnut.odin.formation.entity.RunEntry;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.dto.FormationFrameQuery;
import com.walnut.odin.formation.dto.FormationPageQuery;
import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.dto.FormationRunSubmitResult;
import com.walnut.odin.formation.dto.FormationRuntimeSnapshot;
import com.walnut.odin.formation.dto.FormationGroupTaskAddRequest;
import com.walnut.odin.formation.dto.FormationGroupUpsertRequest;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.entity.GenericGroup;
import com.walnut.odin.formation.entity.GenericGroupTask;
import com.walnut.odin.formation.schedule.FormationRunPreparator;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.source.FrameManipulator;
import com.walnut.odin.formation.source.GroupManipulator;
import com.walnut.odin.formation.source.GroupTaskManipulator;
import com.walnut.odin.formation.source.PageManipulator;
import com.walnut.odin.formation.source.RunManipulator;

public class RavenFormationService implements FormationService {

    protected FormationRunPreparator mRunPreparator;
    protected FormationInstrument    mFormationInstrument;
    protected GroupManipulator       mGroupManipulator;
    protected GroupTaskManipulator   mGroupTaskManipulator;
    protected RunManipulator         mRunManipulator;
    protected PageManipulator        mPageManipulator;
    protected FrameManipulator       mFrameManipulator;
    protected FormationScheduler     mScheduler;
    protected FormationDispatcher    mDispatcher;

    public RavenFormationService(
            FormationRunPreparator runPreparator,
            FormationInstrument formationInstrument,
            FormationScheduler scheduler,
            FormationDispatcher dispatcher
    ) {
        this.mRunPreparator = runPreparator;
        this.mFormationInstrument = formationInstrument;
        this.mGroupManipulator = formationInstrument.masterManipulator().groupManipulator();
        this.mGroupTaskManipulator = formationInstrument.masterManipulator().groupTaskManipulator();
        this.mRunManipulator = formationInstrument.masterManipulator().runManipulator();
        this.mPageManipulator = formationInstrument.masterManipulator().pageManipulator();
        this.mFrameManipulator = formationInstrument.masterManipulator().frameManipulator();
        this.mScheduler = scheduler;
        this.mDispatcher = dispatcher;
    }

    @Override
    public GroupEntry retrieveGroup( GUID formationGuid ) {
        if ( formationGuid == null ) {
            return null;
        }
        return this.mGroupManipulator.selectByGuid( formationGuid );
    }

    @Override
    public GroupEntry retrieveGroupByIdentifier( String identifier ) {
        if ( identifier == null || identifier.trim().isEmpty() ) {
            return null;
        }
        return this.mGroupManipulator.selectByIdentifier( identifier );
    }

    @Override
    public long countGroups( String keyword, String strategyType, Boolean enable ) {
        return this.mGroupManipulator.countGroups( keyword, strategyType, enable );
    }

    @Override
    public List<GroupEntry> pageGroups( String keyword, String strategyType, Boolean enable, long offset, long limit ) {
        return this.mGroupManipulator.pageGroups( keyword, strategyType, enable, Math.max( 0L, offset ), Math.max( 1L, limit ) );
    }

    @Override
    public GroupEntry createGroup( GroupEntry group ) {
        if ( group == null ) {
            throw new IllegalArgumentException( "Formation group is null." );
        }
        this.mGroupManipulator.insert( group );
        return group;
    }

    @Override
    public GroupEntry createGroup( FormationGroupUpsertRequest request ) {
        GenericGroup group = this.createGroupFromRequest( request );
        this.mGroupManipulator.insert( group );
        return group;
    }

    @Override
    public boolean updateGroup( GroupEntry group ) {
        if ( group == null || group.getGuid() == null ) {
            return false;
        }
        return this.mGroupManipulator.update( group ) > 0;
    }

    @Override
    public boolean updateGroup( FormationGroupUpsertRequest request ) {
        GenericGroup group = this.createGroupFromRequest( request );
        if ( group.getGuid() == null ) {
            return false;
        }
        return this.mGroupManipulator.update( group ) > 0;
    }

    @Override
    public boolean updateGroupEnable( GUID formationGuid, boolean enable ) {
        if ( formationGuid == null ) {
            return false;
        }
        return this.mGroupManipulator.updateEnable( formationGuid, enable ) > 0;
    }

    @Override
    public FormationGroupPurgeResult purgeGroup( GUID formationGuid ) {
        List<GUID> formationGuids = new ArrayList<>();
        if ( formationGuid != null ) {
            formationGuids.add( formationGuid );
        }
        return this.purgeGroups( formationGuids );
    }

    @Override
    public FormationGroupPurgeResult purgeGroups( List<GUID> formationGuids ) {
        List<GUID> normalizedGuids = this.normalizeFormationGuids( formationGuids );
        FormationGroupPurgeResult emptyResult = new FormationGroupPurgeResult();
        emptyResult.setRequestedCount( normalizedGuids.size() );
        if ( normalizedGuids.isEmpty() ) {
            return emptyResult;
        }

        return this.mFormationInstrument.masterManipulator().purgeGroups( normalizedGuids );
    }

    @Override
    public GroupTaskEntry retrieveGroupTask( GUID groupTaskGuid ) {
        if ( groupTaskGuid == null ) {
            return null;
        }
        return this.mGroupTaskManipulator.selectByGuid( groupTaskGuid );
    }

    @Override
    public long countGroupTasks( GUID formationGuid, Boolean enable ) {
        if ( formationGuid == null ) {
            return 0L;
        }
        return this.mGroupTaskManipulator.countByFormationGuid( formationGuid, enable );
    }

    @Override
    public long countGroupTasks( GUID formationGuid, Boolean enable, String taskKeyword, String scheduleType ) {
        if ( formationGuid == null ) {
            return 0L;
        }
        return this.mGroupTaskManipulator.countByFormationGuid(
                formationGuid,
                enable,
                taskKeyword,
                scheduleType
        );
    }

    @Override
    public List<GroupTaskEntry> listGroupTasks( GUID formationGuid, Boolean enable ) {
        if ( formationGuid == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mGroupTaskManipulator.listByFormationGuid( formationGuid, enable );
    }

    @Override
    public List<GroupTaskEntry> pageGroupTasks( GUID formationGuid, Boolean enable, long offset, long limit ) {
        if ( formationGuid == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mGroupTaskManipulator.pageByFormationGuid(
                formationGuid,
                enable,
                Math.max( 0L, offset ),
                Math.max( 1L, limit )
        );
    }

    @Override
    public List<GroupTaskEntry> pageGroupTasks(
            GUID formationGuid,
            Boolean enable,
            String taskKeyword,
            String scheduleType,
            long offset,
            long limit ) {
        if ( formationGuid == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mGroupTaskManipulator.pageByFormationGuid(
                formationGuid,
                enable,
                taskKeyword,
                scheduleType,
                Math.max( 0L, offset ),
                Math.max( 1L, limit )
        );
    }

    @Override
    public GroupTaskEntry addGroupTask( GroupTaskEntry groupTask ) {
        if ( groupTask == null ) {
            throw new IllegalArgumentException( "Formation group task is null." );
        }
        this.mGroupTaskManipulator.insert( groupTask );
        return groupTask;
    }

    @Override
    public GroupTaskEntry addGroupTask( FormationGroupTaskAddRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "Formation group task request is null." );
        }

        GenericGroupTask groupTask = new GenericGroupTask();
        if ( request.getGuid() == null ) {
            groupTask.setGuid( this.mFormationInstrument.guidAllocator().nextGUID() );
        } else {
            groupTask.setGuid( request.getGuid() );
        }
        groupTask.setFormationGuid( request.getFormationGuid() );
        groupTask.setTaskGuid( request.getTaskGuid() );
        groupTask.setTaskName( request.getTaskName() );
        groupTask.setScheduleType( request.getScheduleType() );
        groupTask.setSequenceNo( request.getSequenceNo() );
        groupTask.setPriority( request.getPriority() );
        groupTask.setEnable( request.isEnable() );
        this.mGroupTaskManipulator.insert( groupTask );
        return groupTask;
    }

    @Override
    public boolean updateGroupTaskEnable( GUID groupTaskGuid, boolean enable ) {
        if ( groupTaskGuid == null ) {
            return false;
        }
        return this.mGroupTaskManipulator.updateEnable( groupTaskGuid, enable ) > 0;
    }

    @Override
    public boolean removeGroupTask( GUID groupTaskGuid ) {
        if ( groupTaskGuid == null ) {
            return false;
        }
        return this.mGroupTaskManipulator.removeByGuid( groupTaskGuid ) > 0;
    }

    @Override
    public FormationRunSubmitResult submitRun( FormationRunSubmitRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "FormationRunSubmitRequest is null." );
        }

        GenericRun run = (GenericRun)this.mRunPreparator.prepareRun( request );
        FormationRunSubmitResult result = new FormationRunSubmitResult();
        result.setRunGuid( run.getGuid() );
        result.setFormationGuid( run.getFormationGuid() );
        result.setRunStatus( run.getRunStatus() );
        result.setTotalCount( run.getTotalCount() );
        if ( request.isAutoDispatch() && this.mScheduler != null ) {
            this.mScheduler.pulse();
            result.setDispatched( true );
        }
        return result;
    }

    @Override
    public boolean cancelRun( GUID runGuid ) {
        if ( runGuid == null ) {
            return false;
        }
        return this.mRunManipulator.updateStatus( runGuid, FormationRunStatus.Cancelled.getName() ) > 0;
    }

    @Override
    public RunEntry retrieveRun( GUID runGuid ) {
        if ( runGuid == null ) {
            return null;
        }
        return this.mRunManipulator.selectByGuid( runGuid );
    }

    @Override
    public long countRuns( GUID formationGuid, String strategyType, String runStatus ) {
        return this.mRunManipulator.countRuns( formationGuid, strategyType, runStatus );
    }

    @Override
    public List<RunEntry> pageRuns( GUID formationGuid, String strategyType, String runStatus, long offset, long limit ) {
        return this.mRunManipulator.pageRuns( formationGuid, strategyType, runStatus, Math.max( 0L, offset ), Math.max( 1L, limit ) );
    }

    @Override
    public List<FormationPage> listRunPages( GUID runGuid ) {
        if ( runGuid == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mPageManipulator.listByRunGuid( runGuid );
    }

    @Override
    public long countRunPages( FormationPageQuery query ) {
        if ( query == null || query.getRunGuid() == null ) {
            return 0L;
        }
        return this.mPageManipulator.countPages( query );
    }

    @Override
    public List<FormationPage> pageRunPages( FormationPageQuery query, long offset, long limit ) {
        if ( query == null || query.getRunGuid() == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mPageManipulator.pagePages( query, Math.max( 0L, offset ), Math.max( 1L, limit ) );
    }

    @Override
    public long countFrames( GUID runGuid, String frameStatus ) {
        FormationFrameQuery query = new FormationFrameQuery();
        query.setRunGuid( runGuid );
        query.setFrameStatus( frameStatus );
        return this.countFrames( query );
    }

    @Override
    public long countFrames( FormationFrameQuery query ) {
        if ( query == null || query.getRunGuid() == null ) {
            return 0L;
        }
        return this.mFrameManipulator.countFrames( query );
    }

    @Override
    public List<FormationFrame> pageFrames( GUID runGuid, String frameStatus, long offset, long limit ) {
        FormationFrameQuery query = new FormationFrameQuery();
        query.setRunGuid( runGuid );
        query.setFrameStatus( frameStatus );
        return this.pageFrames( query, offset, limit );
    }

    @Override
    public List<FormationFrame> pageFrames( FormationFrameQuery query, long offset, long limit ) {
        if ( query == null || query.getRunGuid() == null ) {
            return java.util.Collections.emptyList();
        }
        return this.mFrameManipulator.pageFrames( query, Math.max( 0L, offset ), Math.max( 1L, limit ) );
    }

    @Override
    public FormationRuntimeSnapshot retrieveRuntimeSnapshot() {
        FormationRuntimeSnapshot snapshot = new FormationRuntimeSnapshot();
        if ( this.mScheduler != null ) {
            snapshot.setSchedulerSnapshot( this.mScheduler.retrieveRuntimeSnapshot() );
        }
        if ( this.mDispatcher != null ) {
            snapshot.setDispatcherSnapshot( this.mDispatcher.retrieveRuntimeSnapshot() );
        }
        return snapshot;
    }

    protected GenericGroup createGroupFromRequest( FormationGroupUpsertRequest request ) {
        if ( request == null ) {
            throw new IllegalArgumentException( "Formation group request is null." );
        }

        GenericGroup group = new GenericGroup();
        if ( request.getGuid() == null ) {
            group.setGuid( this.mFormationInstrument.guidAllocator().nextGUID() );
        } else {
            group.setGuid( request.getGuid() );
        }
        group.setIdentifier( request.getIdentifier() );
        group.setTitle( request.getTitle() );
        group.setProjectGuid( request.getProjectGuid() );
        group.setStrategyType( request.getStrategyType() );
        group.setPageSize( request.getPageSize() );
        group.setFrameSize( request.getFrameSize() );
        group.setWindowSize( request.getWindowSize() );
        group.setInflightLimit( request.getInflightLimit() );
        group.setEnable( request.isEnable() );
        group.setDescription( request.getDescription() );
        return group;
    }

    protected List<GUID> normalizeFormationGuids( List<GUID> formationGuids ) {
        LinkedHashSet<GUID> guidSet = new LinkedHashSet<>();
        if ( formationGuids != null ) {
            for ( GUID guid : formationGuids ) {
                if ( guid != null ) {
                    guidSet.add( guid );
                }
            }
        }
        return new ArrayList<>( guidSet );
    }
}
