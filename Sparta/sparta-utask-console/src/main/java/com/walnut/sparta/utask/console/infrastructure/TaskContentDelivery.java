package com.walnut.sparta.utask.console.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.tritium.Tritium;
import com.walnut.archcraft.ender.EnderHydra;

public class TaskContentDelivery extends EnderHydra implements Pinenut {
    protected SpartaUTASKService spartaUTASKService;

    public TaskContentDelivery(String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public TaskContentDelivery(String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public SpartaUTASKService getSpartaUOFSService(){
        return this.spartaUTASKService;
    }

    @Override
    public void vitalize () throws Exception {
        this.spartaUTASKService = new SpartaUTASKService( "SpartaUTASKService", this );
        this.spartaUTASKService.execute();


        this.getTaskManager().add(this.spartaUTASKService);
        this.getTaskManager().syncWaitingTerminated();
    }
}
