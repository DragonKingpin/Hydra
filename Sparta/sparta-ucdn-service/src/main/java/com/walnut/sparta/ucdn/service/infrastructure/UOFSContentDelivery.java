package com.walnut.sparta.ucdn.service.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.radium.Radium;

public class UOFSContentDelivery extends Radium implements FSContentDeliveryService {
    protected SpartaUCDNService spartaUCDNService;

    public UOFSContentDelivery( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UOFSContentDelivery( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        this.spartaUCDNService = new SpartaUCDNService( "SpartaUCDNService", this );
        this.spartaUCDNService.execute();


        this.getTaskManager().add(this.spartaUCDNService);
        this.getTaskManager().syncWaitingTerminated();
    }
}
