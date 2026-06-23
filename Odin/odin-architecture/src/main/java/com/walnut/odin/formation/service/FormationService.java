package com.walnut.odin.formation.service;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.dto.FormationRunSubmitRequest;
import com.walnut.odin.formation.dto.FormationRunSubmitResult;
import com.walnut.odin.formation.dto.FormationRuntimeSnapshot;
import com.walnut.odin.formation.dto.FormationGroupTaskAddRequest;
import com.walnut.odin.formation.dto.FormationGroupUpsertRequest;
import com.walnut.odin.formation.entity.GroupEntry;
import com.walnut.odin.formation.entity.GroupTaskEntry;
import com.walnut.odin.formation.entity.RunEntry;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationPage;

public interface FormationService extends Pinenut {

    GroupEntry retrieveGroup( GUID formationGuid );

    GroupEntry retrieveGroupByIdentifier( String identifier );

    long countGroups( String keyword, String strategyType, Boolean enable );

    List<GroupEntry> pageGroups( String keyword, String strategyType, Boolean enable, long offset, long limit );

    GroupEntry createGroup( GroupEntry group );

    GroupEntry createGroup( FormationGroupUpsertRequest request );

    boolean updateGroup( GroupEntry group );

    boolean updateGroup( FormationGroupUpsertRequest request );

    boolean updateGroupEnable( GUID formationGuid, boolean enable );

    GroupTaskEntry retrieveGroupTask( GUID groupTaskGuid );

    long countGroupTasks( GUID formationGuid, Boolean enable );

    List<GroupTaskEntry> listGroupTasks( GUID formationGuid, Boolean enable );

    GroupTaskEntry addGroupTask( GroupTaskEntry groupTask );

    GroupTaskEntry addGroupTask( FormationGroupTaskAddRequest request );

    boolean updateGroupTaskEnable( GUID groupTaskGuid, boolean enable );

    boolean removeGroupTask( GUID groupTaskGuid );

    FormationRunSubmitResult submitRun( FormationRunSubmitRequest request );

    boolean cancelRun( GUID runGuid );

    RunEntry retrieveRun( GUID runGuid );

    long countRuns( GUID formationGuid, String strategyType, String runStatus );

    List<RunEntry> pageRuns( GUID formationGuid, String strategyType, String runStatus, long offset, long limit );

    List<FormationPage> listRunPages( GUID runGuid );

    long countFrames( GUID runGuid, String frameStatus );

    List<FormationFrame> pageFrames( GUID runGuid, String frameStatus, long offset, long limit );

    FormationRuntimeSnapshot retrieveRuntimeSnapshot();
}
