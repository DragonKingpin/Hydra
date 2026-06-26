package com.walnut.odin.formation;

import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.service.FlowService;
import com.walnut.odin.formation.service.FormationFlowService;
import com.walnut.odin.formation.service.FormationRunService;
import com.walnut.odin.formation.service.RunService;
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
            MasterManipulator masterManipulator
    ) {
        this.mConfig = config;
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = masterManipulator;
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
