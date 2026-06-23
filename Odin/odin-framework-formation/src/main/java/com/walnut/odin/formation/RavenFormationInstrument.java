package com.walnut.odin.formation;

import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.service.FlowService;
import com.walnut.odin.formation.service.FormationFlowService;
import com.walnut.odin.formation.service.FormationRunService;
import com.walnut.odin.formation.service.RunService;
import com.walnut.odin.formation.source.FormationGroupMapper;
import com.walnut.odin.formation.source.FormationGroupTaskMapper;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;
import com.walnut.odin.formation.source.KernelMasterManipulator;
import com.walnut.odin.formation.source.MasterManipulator;

public class RavenFormationInstrument implements FormationInstrument {

    protected FormationConfig          mConfig;
    protected GuidAllocator            mGuidAllocator;
    protected MasterManipulator        mMasterManipulator;

    protected RunService               mRunService;
    protected FlowService              mFlowService;

    public RavenFormationInstrument(
            FormationConfig config,
            GuidAllocator guidAllocator,
            FormationGroupMapper groupMapper,
            FormationGroupTaskMapper groupTaskMapper,
            FormationRunMapper runMapper,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper
    ) {
        this.mConfig = config;
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = new KernelMasterManipulator(
                groupMapper,
                groupTaskMapper,
                runMapper,
                pageMapper,
                frameMapper
        );
        this.prepareServices();
    }

    protected void prepareServices() {
        this.mRunService = new FormationRunService(
                this.mGuidAllocator,
                this.mMasterManipulator
        );
        this.mFlowService = new FormationFlowService(
                this.mGuidAllocator,
                this.mMasterManipulator
        );
    }

    @Override
    public FormationConfig formationConfig() {
        return this.mConfig;
    }

    @Override
    public GuidAllocator guidAllocator() {
        return this.mGuidAllocator;
    }

    @Override
    public MasterManipulator masterManipulator() {
        return this.mMasterManipulator;
    }

    @Override
    public RunService runService() {
        return this.mRunService;
    }

    @Override
    public FlowService flowService() {
        return this.mFlowService;
    }
}
