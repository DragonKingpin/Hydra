package com.walnut.odin.formation.strategy.fixed;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.walnut.odin.formation.FormationStrategyContext;
import com.walnut.odin.formation.FormationStrategyRuntime;
import com.walnut.odin.formation.flow.ArchFormationProducer;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationPage;
import com.walnut.odin.formation.plan.GenericFormationFrame;
import com.walnut.odin.formation.plan.GenericFormationPage;
import com.walnut.odin.formation.source.FormationRunFrameMapper;
import com.walnut.odin.formation.source.FormationRunPageMapper;

public class FixedPageFormationProducer64 extends ArchFormationProducer<FormationPage>
        implements FixedPageFormationProducer {
    protected FormationStrategyContext mContext;
    protected FormationStrategyRuntime mRuntime;
    protected GUID                     mRunGuid;
    protected GuidAllocator            mGuidAllocator;
    protected FormationRunPageMapper   mPageMapper;
    protected FormationRunFrameMapper  mFrameMapper;
    protected long                     mnLeaseSeconds = 300L;

    public FixedPageFormationProducer64(
            FormationStrategyContext context,
            FormationStrategyRuntime runtime,
            GUID runGuid,
            GuidAllocator guidAllocator,
            FormationRunPageMapper pageMapper,
            FormationRunFrameMapper frameMapper,
            String claimOwner,
            long productsSum
    ) {
        super( claimOwner, productsSum );
        this.mContext = context;
        this.mRuntime = runtime;
        this.mRunGuid = runGuid;
        this.mGuidAllocator = guidAllocator;
        this.mPageMapper = pageMapper;
        this.mFrameMapper = frameMapper;
    }

    @Override
    public FormationPage require() {
        List<GenericFormationPage> pages = this.mPageMapper.fetchPendingPages( this.mRunGuid, 1L );
        if ( pages == null || pages.isEmpty() ) {
            return null;
        }

        GenericFormationPage page = pages.get( 0 );
        GUID claimToken = this.mGuidAllocator.nextGUID();
        if ( this.mPageMapper.claimPage( page.getId(), this.mszClaimOwner, claimToken, this.mnLeaseSeconds ) <= 0 ) {
            return null;
        }
        this.mPageMapper.markRunning( page.getId() );

        List<GenericFormationFrame> frames = this.mFrameMapper.fetchPendingFramesByPage(
                this.mRunGuid, page.getPageNo()
        );
        page.setFrames( (List<FormationFrame>)(List<?>)frames );
        if ( this.mRuntime instanceof com.walnut.odin.formation.GenericFormationStrategyRuntime ) {
            ( (com.walnut.odin.formation.GenericFormationStrategyRuntime)this.mRuntime ).increaseProducedCount();
        }
        return page;
    }

    @Override
    public void deactivate( FormationPage product ) {
        if ( product instanceof GenericFormationPage ) {
            this.mPageMapper.markCompleted( ( (GenericFormationPage)product ).getId() );
        }
        this.countDown();
    }

    @Override
    public boolean hasMoreProducts() {
        return !this.isFinished();
    }
}
