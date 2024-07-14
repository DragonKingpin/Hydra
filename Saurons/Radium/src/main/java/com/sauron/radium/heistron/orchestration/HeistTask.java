package com.sauron.radium.heistron.orchestration;

import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Executum;
import com.sauron.radium.heistron.CascadeHeist;
import com.sauron.radium.heistron.Heistum;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class HeistTask extends ArchProcessum implements Taskium {
    private static final AtomicInteger rootAutoIncrementId     = new AtomicInteger( 0 );

    private static int nextRootAutoIncrementId() {
        return Executum.AutoIncrementId.getAndIncrement();
    }

    private static String name( String szName, Heistum heistum ) {
        if( szName != null ) {
            return szName;
        }

        if( heistum instanceof CascadeHeist ) {
            CascadeHeist cascadeHeist = (CascadeHeist) heistum;
            return cascadeHeist.getInstanceFullName();
        }

        return heistum.heistName();
    }

    private int          mnTaskId;
    protected Heistum    mParentHeist;

    public HeistTask( String szName, Heistum heistum ) {
        super( szName, heistum.getHeistgram() );

        this.mParentHeist        = heistum;
        //this.mTaskManager        = new GenericMasterTaskManager( this );
        this.mTaskManager        = new LocalChildHeistOrchestrator( this, this.getParentHeist().getConfig() );


        boolean bUsingRootId     = true ;
        if( heistum instanceof CascadeHeist ) {
            CascadeHeist cascadeHeist = (CascadeHeist) heistum;
            if( cascadeHeist.parent() != null ){
                this.mnTaskId    = cascadeHeist.parent().getHeistium().getHeistletOrchestrator().nextAutoIncrementTaskId();
                bUsingRootId    = false;
            }
        }

        if( bUsingRootId ) {
            this.mnTaskId        = HeistTask.nextRootAutoIncrementId();
        }
        this.mszName            = szName + "-task-" + this.getTaskId();
    }

    public HeistTask( Heistum heistum ) {
        this( HeistTask.name( null, heistum ), heistum );
    }

    @Override
    public int getTaskId() {
        return this.mnTaskId;
    }

    public Heistum getParentHeist() {
        return this.mParentHeist;
    }

}
