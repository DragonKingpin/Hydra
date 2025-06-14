package com.walnut.sparta.utask.console.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.tritium.Tritium;

public class UTASKContentDelivery extends Tritium implements UTASKContentDeliveryService {
    protected SpartaUTASKService spartaUTASKService;

    public UTASKContentDelivery(String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UTASKContentDelivery(String[] args, String szName, CascadeSystem parent ){
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
