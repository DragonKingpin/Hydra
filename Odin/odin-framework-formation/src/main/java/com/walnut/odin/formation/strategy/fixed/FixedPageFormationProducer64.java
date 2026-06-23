package com.walnut.odin.formation.strategy.fixed;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;
import com.walnut.odin.formation.flow.ArchFormationProducer;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.plan.GenericFormationPage;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.formation.strategy.GenericFormationStrategyRuntime;

public class FixedPageFormationProducer64 extends ArchFormationProducer<FormationPage>
        implements FixedPageFormationProducer {
    protected FormationStrategyContext mContext;
    protected FormationStrategyRuntime mRuntime;
    protected GUID                     mRunGuid;
    protected GuidAllocator            mGuidAllocator;
    protected MasterManipulator        mMasterManipulator;
    protected long                     mnLeaseSeconds = 300L;

    public FixedPageFormationProducer64(
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
        List<? extends FormationPage> pages = this.mMasterManipulator.pageManipulator().fetchPendingPages( this.mRunGuid, 1L );
        if ( pages == null || pages.isEmpty() ) {
            return null;
        }

        GenericFormationPage page = (GenericFormationPage)pages.get( 0 );
        GUID claimToken = this.mGuidAllocator.nextGUID();
        if ( this.mMasterManipulator.pageManipulator().claimPage( page.getId(), this.mszClaimOwner, claimToken, this.mnLeaseSeconds ) <= 0 ) {
            return null;
        }
        this.mMasterManipulator.pageManipulator().markRunning( page.getId() );

        List<? extends FormationFrame> frames = this.mMasterManipulator.frameManipulator().fetchPendingFramesByPage(
                this.mRunGuid, page.getPageNo()
        );
        page.setFrames( (List<FormationFrame>)(List<?>)frames );
        if ( this.mRuntime instanceof GenericFormationStrategyRuntime) {
            ( (GenericFormationStrategyRuntime)this.mRuntime ).increaseProducedCount();
        }
        return page;
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
