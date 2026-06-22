package com.walnut.odin.formation;

import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.formation.dispatch.FormationDispatcher;
import com.walnut.odin.formation.dispatch.LocalFormationDispatcher;
import com.walnut.odin.formation.recovery.FormationRunReconciler;
import com.walnut.odin.formation.recovery.LightweightFormationRunReconciler;
import com.walnut.odin.formation.schedule.FormationRunPreparator;
import com.walnut.odin.formation.schedule.FormationScheduler;
import com.walnut.odin.formation.schedule.KernelFormationRunPreparator;
import com.walnut.odin.formation.schedule.RavenFormationScheduler;
import com.walnut.odin.formation.service.FormationFlowService;
import com.walnut.odin.formation.service.FormationRunService;
import com.walnut.odin.formation.service.FormationService;
import com.walnut.odin.formation.service.RavenFormationService;
import com.walnut.odin.formation.source.FormationGroupMapper;
import com.walnut.odin.formation.source.FormationGroupTaskMapper;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;

public class RavenFormationManager implements FormationManager {

    protected FormationConfig          mConfig;
    protected GuidAllocator            mGuidAllocator;
    protected UniformTaskScheduler     mTaskScheduler;
    protected FormationGroupMapper     mGroupMapper;
    protected FormationGroupTaskMapper mGroupTaskMapper;
    protected FormationRunMapper       mRunMapper;
    protected FormationRunPageMapper   mPageMapper;
    protected FormationRunFrameMapper  mFrameMapper;

    protected FormationRunService      mRunService;
    protected FormationFlowService     mFlowService;
    protected FormationRunPreparator   mRunPreparator;
    protected FormationRunReconciler   mRunReconciler;
    protected FormationDispatcher      mDispatcher;
    protected FormationScheduler       mScheduler;
    protected FormationService         mService;

    public RavenFormationManager(
            FormationConfig config,
            GuidAllocator guidAllocator,
            UniformTaskScheduler taskScheduler,
            FormationGroupMapper groupMapper,
            FormationGroupTaskMapper groupTaskMapper,
            FormationRunMapper runMapper,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper
    ) {
        this.mConfig = config;
        this.mGuidAllocator = guidAllocator;
        this.mTaskScheduler = taskScheduler;
        this.mGroupMapper = groupMapper;
        this.mGroupTaskMapper = groupTaskMapper;
        this.mRunMapper = runMapper;
        this.mPageMapper = pageMapper;
        this.mFrameMapper = frameMapper;
    }

    @Override
    public void prepareFormation() {
        this.mRunService = new FormationRunService(
                this.mGuidAllocator,
                this.mGroupMapper,
                this.mGroupTaskMapper,
                this.mRunMapper,
                this.mPageMapper,
                this.mFrameMapper
        );
        this.mFlowService = new FormationFlowService(
                this.mGuidAllocator,
                this.mRunMapper,
                this.mPageMapper,
                this.mFrameMapper
        );
        this.mDispatcher = new LocalFormationDispatcher( this.mConfig );
        this.mRunReconciler = new LightweightFormationRunReconciler( this.mConfig );
        this.mScheduler = new RavenFormationScheduler(
                this.mConfig,
                this.mRunMapper,
                this.mFlowService,
                this.mTaskScheduler,
                this.mDispatcher,
                this.mRunReconciler
        );
        this.mRunPreparator = new KernelFormationRunPreparator( this.mRunService );
        this.mService = new RavenFormationService(
                this.mRunPreparator,
                this.mRunMapper,
                this.mScheduler,
                this.mDispatcher
        );
    }

    @Override
    public void startupFormation() {
        if ( this.mService == null ) {
            this.prepareFormation();
        }
        this.mDispatcher.startup();
        this.mScheduler.startup();
    }

    @Override
    public void shutdownFormation() {
        if ( this.mScheduler != null ) {
            this.mScheduler.shutdown();
        }
        if ( this.mDispatcher != null ) {
            this.mDispatcher.shutdown();
        }
    }

    @Override
    public FormationService formationService() {
        return this.mService;
    }

    @Override
    public FormationScheduler formationScheduler() {
        return this.mScheduler;
    }

    @Override
    public FormationDispatcher formationDispatcher() {
        return this.mDispatcher;
    }
}
