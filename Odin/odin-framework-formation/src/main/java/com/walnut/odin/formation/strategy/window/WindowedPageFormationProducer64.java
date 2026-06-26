package com.walnut.odin.formation.strategy.window;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.flow.ArchFormationProducer;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.plan.GenericFormationPage;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;

public class WindowedPageFormationProducer64 extends ArchFormationProducer<FormationPage>
        implements WindowedPageFormationProducer {
    protected FormationStrategyContext mContext;
    protected FormationStrategyRuntime mRuntime;
    protected GUID                     mRunGuid;
    protected GuidAllocator            mGuidAllocator;
    protected MasterManipulator        mMasterManipulator;
    protected long                     mnLeaseSeconds = 300L;
    protected Queue<FormationPage>     mWindow = new ArrayDeque<>();

    public WindowedPageFormationProducer64(
            FormationStrategyContext context,
            FormationStrategyRuntime runtime,
            GUID runGuid,
            GuidAllocator guidAllocator,
            MasterManipulator masterManipulator,
            String claimOwner,
            long productsSum
    ) {
        super( claimOwner, productsSum );
        this.mContext = context;
        this.mRuntime = runtime;
        this.mRunGuid = runGuid;
        this.mGuidAllocator = guidAllocator;
        this.mMasterManipulator = masterManipulator;
    }

    @Override
    public FormationPage require() {
        this.fillWindow();
        return this.mWindow.poll();
    }

    protected void fillWindow() {
        long windowSize = Math.max( 1L, this.mRuntime.windowSize() );
        long shortage = windowSize - this.mWindow.size();
        if ( shortage <= 0L ) {
            return;
        }

        List<? extends FormationPage> pages = this.mMasterManipulator.pageManipulator().fetchPendingPages( this.mRunGuid, shortage );
        if ( pages == null || pages.isEmpty() ) {
            return;
        }

        for ( FormationPage pendingPage : pages ) {
            GenericFormationPage page = (GenericFormationPage)pendingPage;
            GUID claimToken = this.mGuidAllocator.nextGUID();
            if ( this.mMasterManipulator.pageManipulator().claimPage( page.getId(), this.mszClaimOwner, claimToken, this.mnLeaseSeconds ) <= 0 ) {
                continue;
            }
            this.mMasterManipulator.pageManipulator().markRunning( page.getId() );
            List<? extends FormationFrame> frames = this.mMasterManipulator.frameManipulator().fetchPendingFramesByPage(
                    this.mRunGuid, page.getPageNo()
            );
            page.setFrames( new ArrayList<FormationFrame>( frames ) );
            this.mWindow.offer( page );
            if ( this.mRuntime instanceof GenericFormationStrategyRuntime ) {
                ( (GenericFormationStrategyRuntime)this.mRuntime ).increaseProducedCount();
            }
        }
    }

    @Override
    public void deactivate( FormationPage product ) {
        if ( product instanceof GenericFormationPage ) {
            this.mMasterManipulator.pageManipulator().markCompleted( ( (GenericFormationPage)product ).getId() );
        }
        this.countDown();
    }

    @Override
    public boolean hasMoreProducts() {
        return !this.isFinished();
    }
}
