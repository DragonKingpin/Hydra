package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.radium.Radium;

public class UCDNContentDelivery extends Radium implements FSContentDeliveryService {
    protected SpartaUCDNService spartaUCDNService;

    public UCDNContentDelivery(String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public UCDNContentDelivery(String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    public SpartaUCDNService getSpartaUCDNService(){
        return this.spartaUCDNService;
    }

    @Override
    public void vitalize () throws Exception {
        this.spartaUCDNService = new SpartaUCDNService( "SpartaUCDNService", this );
        this.spartaUCDNService.execute();


        this.getTaskManager().add(this.spartaUCDNService);
        this.getTaskManager().syncWaitingTerminated();
    }
}
