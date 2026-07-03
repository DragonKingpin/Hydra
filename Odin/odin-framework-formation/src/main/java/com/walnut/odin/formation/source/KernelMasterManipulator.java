package com.walnut.odin.formation.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.deletion.FormationGroupPurgeResult;
import com.walnut.odin.formation.mapper.OdinFormationMappingDriver;

public class KernelMasterManipulator implements MasterManipulator {

    protected OdinFormationMappingDriver mMappingDriver;
    protected GroupManipulator     mGroupManipulator;
    protected GroupTaskManipulator mGroupTaskManipulator;
    protected RunManipulator       mRunManipulator;
    protected PageManipulator      mPageManipulator;
    protected FrameManipulator     mFrameManipulator;

    public KernelMasterManipulator(
            OdinFormationMappingDriver mappingDriver,
            GroupManipulator groupManipulator,
            GroupTaskManipulator groupTaskManipulator,
            RunManipulator runManipulator,
            PageManipulator pageManipulator,
            FrameManipulator frameManipulator
    ) {
        this.mMappingDriver = mappingDriver;
        this.mGroupManipulator = groupManipulator;
        this.mGroupTaskManipulator = groupTaskManipulator;
        this.mRunManipulator = runManipulator;
        this.mPageManipulator = pageManipulator;
        this.mFrameManipulator = frameManipulator;
    }

    @Override
    public GroupManipulator groupManipulator() {
        return this.mGroupManipulator;
    }

    @Override
    public GroupTaskManipulator groupTaskManipulator() {
        return this.mGroupTaskManipulator;
    }

    @Override
    public RunManipulator runManipulator() {
        return this.mRunManipulator;
    }

    @Override
    public PageManipulator pageManipulator() {
        return this.mPageManipulator;
    }

    @Override
    public FrameManipulator frameManipulator() {
        return this.mFrameManipulator;
    }

    @Override
    public FormationGroupPurgeResult purgeGroups( List<GUID> formationGuids ) {
        FormationGroupPurgeResult result = new FormationGroupPurgeResult();
        result.setRequestedCount( formationGuids == null ? 0 : formationGuids.size() );
        if ( formationGuids == null || formationGuids.isEmpty() ) {
            return result;
        }

        return this.mMappingDriver.transaction().required( scope -> {
            FormationGroupPurgeResult purgeResult = new FormationGroupPurgeResult();
            purgeResult.setRequestedCount( formationGuids.size() );
            purgeResult.setRemovedFrameCount(
                    scope.mapper( FormationRunFrameMapper.class ).removeByFormationGuids( formationGuids )
            );
            purgeResult.setRemovedPageCount(
                    scope.mapper( FormationRunPageMapper.class ).removeByFormationGuids( formationGuids )
            );
            purgeResult.setRemovedRunCount(
                    scope.mapper( FormationRunMapper.class ).removeByFormationGuids( formationGuids )
            );
            purgeResult.setRemovedGroupTaskCount(
                    scope.mapper( FormationGroupTaskMapper.class ).removeByFormationGuids( formationGuids )
            );
            purgeResult.setRemovedGroupCount(
                    scope.mapper( FormationGroupMapper.class ).removeByGuids( formationGuids )
            );
            return purgeResult;
        } );
    }
}
