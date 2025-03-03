package com.walnuts.sparta.uofs.console.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.radium.Radium;

public class UOFSContentDelivery extends Radium implements UOFSContentDeliveryService{
    protected SpartaUOFSService spartaUOFSService;

    public UOFSContentDelivery(String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UOFSContentDelivery(String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public SpartaUOFSService getSpartaUOFSService(){
        return this.spartaUOFSService;
    }

    @Override
    public void vitalize () throws Exception {
        this.spartaUOFSService = new SpartaUOFSService( "SpartaUOFSService", this );
        this.spartaUOFSService.execute();


        this.getTaskManager().add(this.spartaUOFSService);
        this.getTaskManager().syncWaitingTerminated();
    }
}
