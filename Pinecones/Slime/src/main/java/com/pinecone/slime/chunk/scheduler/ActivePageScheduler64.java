package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;
import com.pinecone.slime.chunk.RangedPage;

import java.util.ArrayList;
import java.util.Map;

public abstract class ActivePageScheduler64 extends RangedPageScheduler64 implements ActivePageScheduler {
    protected ChunkRegister<Long, ContiguousPage> mChunkRegister;

    protected ActivePageScheduler64( RangedPage masterPage, PagePool pagePool, PageDivider divider, long autoIncrementId ) {
        super( masterPage, pagePool, divider, autoIncrementId );
    }

    protected ActivePageScheduler64( PageDivider divider, long autoIncrementId ) {
        super( divider, autoIncrementId );
    }

    @Override
    protected void beforeActivatePage() {
        if( this.mRecycleStrategy == null ) {
            return;
        }

        ArrayList<ContiguousPage> badPages = null;
        for ( Map.Entry kv : this.mChunkRegister.entrySet() ) {
            if( this.mRecycleStrategy.qualified( (ContiguousPage)kv.getValue() )  ) {
                if( badPages == null ) {
                    badPages = new ArrayList<>();
                }

                badPages.add( (ContiguousPage)kv.getValue() );
            }
        }

        if( badPages != null ) {
            for ( ContiguousPage p : badPages ) {
                this.deactivate( p );
            }
        }
    }

    @Override
    public ContiguousPage activate() {
        this.beforeActivatePage();

        ContiguousPage page = (ContiguousPage) this.getDivider().allocate();
        this.activate( page );
        return page;
    }

    @Override
    public void activate( ContiguousPage that ) {
        this.mChunkRegister.put( that.getId(), that );
    }

    @Override
    public void deactivate( ContiguousPage that ) {
        this.mChunkRegister.remove( that.getId() );
        this.mPagePool.deallocate( that );
    }

    @Override
    public void deactivate( ContiguousPage[] those ){
        for ( ContiguousPage p : those ) {
            this.deactivate( p );
        }
    }

    @Override
    public long getActivatedSize() {
        return this.mChunkRegister.size();
    }

    @Override
    public ContiguousPage getPageById(long id ){
        return this.mChunkRegister.get( id );
    }
}
